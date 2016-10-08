/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.domain.enums.InvoiceStatus;
import com.forms.PaymentForm;
import com.service.PaymentService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.service.SettingsService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepository paymentRepo;    
    private InvoiceRepository invoiceRepo;
    private AccountRepository accountRepo;
    private SettingsService settingsService;
    private ModifiedPaymentRepository modifiedPaymentRepo;
    private ScheduleRepository schedRepo;
    
    @Autowired 
    public PaymentServiceImpl(PaymentRepository paymentRepo, InvoiceRepository invoiceRepo, 
                              AccountRepository accountRepo, SettingsService settingsService,
                              ModifiedPaymentRepository modifiedPaymentRepo, ScheduleRepository schedRepo) {
        this.paymentRepo = paymentRepo;
        this.invoiceRepo = invoiceRepo;
        this.accountRepo = accountRepo;
        this.settingsService = settingsService;
        this.modifiedPaymentRepo = modifiedPaymentRepo;
        this.schedRepo = schedRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public JRDataSource paymentHistoryDataSource(List<Long> paymentIds) {
        List<Payment> report = new ArrayList();
        report.add(new Payment());
        for(Long id : paymentIds)
            report.add(paymentRepo.findById(id));
        return new JRBeanCollectionDataSource(report);
    }

    @Override
    @Transactional
    public Payment save(PaymentForm form) {
        boolean updateFlag = false;
        ModifiedPayment modifiedPayment = null;
        Long version = form.getPayment().getVersion();
        if(form.getPayment().getId() != null){
            updateFlag = true;
            Payment oldPayment = paymentRepo.findOne(form.getPayment().getId());
            modifiedPayment = new ModifiedPayment(oldPayment);
            oldPayment.setAmountPaid(form.getPayment().getAmountPaid());
            oldPayment.setDate(form.getPayment().getDate());
            oldPayment.setVersion(form.getPayment().getVersion());
            oldPayment.setReceiptNumber(form.getPayment().getReceiptNumber());
            form.setPayment(oldPayment);
        }
        Account account = accountRepo.findOne(form.getAccountId());
        Invoice invoice = invoiceRepo.findTopByAccountOrderByIdDesc(account);
        Payment payment = form.getPayment();
        int paymentYear = payment.getDate().getYear(),
                paymentMonth = payment.getDate().getMonthOfYear();
        if(paymentMonth == 1){
            paymentMonth = 12;
            paymentYear -= 1;
        } else paymentMonth -= 1;
        Schedule sched = schedRepo.findByMonthAndYear(paymentMonth, paymentYear);
        account.getPayments().add(payment);
        payment.setSchedule(sched);
        payment.setInvoice(invoice);
        payment.setAccount(account);
        invoice.setPayment(payment);
        payment = paymentRepo.save(payment);
        if(updateFlag && payment.getVersion().compareTo(version) > 0){
            modifiedPayment.setPayment(payment);
            modifiedPaymentRepo.save(modifiedPayment);
        }
        if(version == null || payment.getVersion().compareTo(version) > 0 )
            payment = updateAccountFromPayment(payment);
        return payment;
    }

    @Override
    public Payment updateAccountFromPayment(Payment payment) {
        BigDecimal diff = payment.getInvoice().getNetCharge().subtract(payment.getAmountPaid());
        payment.getAccount().setAccountStandingBalance(diff);
        Settings settings = settingsService.getCurrentSettings();
        if(diff.compareTo(payment.getInvoice().getNetCharge()) == 0){
            payment.getInvoice().setStatus(InvoiceStatus.DEBT);
        }
        else if(diff.doubleValue() > 0)
            payment.getInvoice().setStatus(InvoiceStatus.PARTIALLYPAID);
        else
            payment.getInvoice().setStatus(InvoiceStatus.FULLYPAID);
        int dayDiff = new Period(payment.getInvoice().getDueDate(), payment.getDate()).getDays();
        if(dayDiff > 0 ){
            BigDecimal penalty = payment.getInvoice().getNetCharge().multiply(new BigDecimal(settings.getPenalty()));
            payment.getAccount().setPenalty(new BigDecimal(dayDiff).multiply(penalty));
            if(payment.getAccount().getPenalty().compareTo(new BigDecimal(100)) > 0)
                payment.getAccount().setPenalty(new BigDecimal(100));
        } else {
            payment.getAccount().setPenalty(BigDecimal.ZERO);
        }
        payment = paymentRepo.save(payment);
        if(isAllowedToSetWarningToAccount(payment.getAccount(), settings.getDebtsAllowed()))
            payment.getAccount().setStatus(AccountStatus.WARNING);
        else payment.getAccount().setStatus(AccountStatus.ACTIVE);
        payment.getAccount().setStatusUpdated(true);
        accountRepo.save(payment.getAccount());
        return payment;
    }
    
    @Transactional(readOnly=true)
    @Override
    public Errors validate(PaymentForm paymentForm, Errors errors) {
        Account account = accountRepo.findOne(paymentForm.getAccountId());
        if(account == null)
            errors.reject("global","Account does not exists");
        Invoice invoice = invoiceRepo.findTopByAccountOrderByIdDesc(account);
        if(invoice == null)            
            errors.reject("global","No existing bill for this account");
        else{
            boolean validAmount = (paymentForm.getPayment().getAmountPaid().doubleValue() >= 0) &&
                    (paymentForm.getPayment().getAmountPaid().doubleValue()
                        <= invoice.getNetCharge().doubleValue());
            if(!invoice.getStatus().equals(InvoiceStatus.UNPAID) && paymentForm.getPayment().getId() == null)
                errors.reject("global", "No existing unpaid bill for this account");
            if(!validAmount){
                errors.rejectValue("payment.amountPaid","","Invalid amount");
            }
            if(paymentForm.getPayment().getAmountPaid().doubleValue() != 0){
                if(paymentForm.getPayment().getReceiptNumber().isEmpty()) {
                    errors.rejectValue("payment.receiptNumber", "", "");
                    errors.reject("global", "OR number is required for not DEBT payment");
                }
                else {
                    Payment paymentUniqueOr = paymentRepo.findByReceiptNumber(paymentForm.getPayment().getReceiptNumber());
                    if(paymentUniqueOr != null) {
                        if(paymentForm.getPayment().getId() == null)
                            errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
                        else {
                            Payment origPayment = paymentRepo.findOne(paymentForm.getPayment().getId());
                            if(!paymentUniqueOr.getId().equals(origPayment.getId()))
                                errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
                        }
                    }
                }
            } else {
                if(!paymentForm.getPayment().getReceiptNumber().isEmpty()){
                    errors.rejectValue("payment.receiptNumber", "", "");
                    errors.reject("global", "OR number should be left empty for DEBT payment");
                }
            }
            int paymentYear = paymentForm.getPayment().getDate().getYear(),
                    paymentMonth = paymentForm.getPayment().getDate().getMonthOfYear();
            if(paymentMonth == 1){
                paymentMonth = 12;
                paymentYear--;
            } else paymentMonth --;
            if(schedRepo.findByMonthAndYear(paymentMonth, paymentYear) == null){
                errors.rejectValue("payment.date", "", "Invalid date");
            }
        }
        return errors;
    }

    @Transactional(readOnly=true)
    @Override
    public Payment findPaymentById(Long id) {
        return paymentRepo.findById(id);
    }

    @Transactional(readOnly=true)
    @Override
    public boolean isAllowedToSetWarningToAccount(Account account, Integer debtsAllowed) {
        int ctr = 0;
        for(Invoice invoice: invoiceRepo.findByAccountOrderByIdDesc(account, new PageRequest(0, debtsAllowed))){
            if(! (invoice.getStatus().equals(InvoiceStatus.UNPAID) || invoice.getStatus().equals(InvoiceStatus.DEBT)))
                break;
            else ctr++;
        }
        return debtsAllowed.compareTo(ctr) == 0;
    }

    @Transactional
    @Override
    public List<Account> updateAccountsWithNoPayments(Address address) {
        List<Account> updated = new ArrayList<Account>();
        Settings currentSettings = settingsService.getCurrentSettings();
        List<Address> list = new ArrayList();
        list.add(address);
        for(Account account: accountRepo.findByAddressInAndStatusIn(list, Arrays.asList(AccountStatus.ACTIVE)) ){
            account.setStatusUpdated(true);
            account.setPenalty(BigDecimal.ZERO);
            if(isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed()))
                account.setStatus(AccountStatus.WARNING);
            updated.add(accountRepo.save(account));
        }
        return updated;
    }
}