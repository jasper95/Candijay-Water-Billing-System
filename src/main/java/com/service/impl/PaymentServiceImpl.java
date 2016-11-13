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
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        Account account = accountRepo.findOne(form.getAccountId());
        BigDecimal oldBalance = BigDecimal.ZERO.add(account.getAccountStandingBalance()),
                                oldPenalty = BigDecimal.ZERO.add(account.getPenalty());
        Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
        Settings settings = settingsService.getCurrentSettings();
        if(form.getPayment().getId() != null){
            updateFlag = true;
            Payment oldPayment = paymentRepo.findOne(form.getPayment().getId());
            modifiedPayment = new ModifiedPayment(oldPayment);
            oldBalance = oldBalance.add(oldPayment.getAmountPaid());
            oldPenalty = oldPenalty.subtract(calculatePenalty(invoice.getDueDate(),
                    oldPayment.getDate(), oldBalance, new BigDecimal(settings.getPenalty())));
            oldPayment.setAmountPaid(form.getPayment().getAmountPaid());
            oldPayment.setDate(form.getPayment().getDate());
            oldPayment.setVersion(form.getPayment().getVersion());
            oldPayment.setReceiptNumber(form.getPayment().getReceiptNumber());
            form.setPayment(oldPayment);
        }
        Payment payment = form.getPayment();
        int paymentYear = payment.getDate().getYear(),
                paymentMonth = payment.getDate().getMonthOfYear();
        if(paymentMonth == 1){
            paymentMonth = 12;
            paymentYear --;
        } else paymentMonth --;
        Schedule sched = schedRepo.findByMonthAndYear(paymentMonth, paymentYear);
        account.getPayments().add(payment);
        payment.setSchedule(sched);
        payment.setInvoice(invoice);
        payment.setAccount(account);
        invoice.getPayments().add(payment);
        payment = paymentRepo.save(payment);
        if(updateFlag && payment.getVersion().compareTo(version) > 0){
            modifiedPayment.setPayment(payment);
            modifiedPaymentRepo.save(modifiedPayment);
        }
        if(version == null || payment.getVersion().compareTo(version) > 0 ) {
            account.setPenalty(oldPenalty.add(calculatePenalty(invoice.getDueDate(), payment.getDate(), oldBalance, new BigDecimal(settings.getPenalty()))));
            BigDecimal remainingBalance = oldBalance.subtract(payment.getAmountPaid());
            if(remainingBalance.compareTo(payment.getInvoice().getNetCharge()) == 0)
                payment.getInvoice().setStatus(InvoiceStatus.DEBT);
            else if(remainingBalance.compareTo(BigDecimal.ZERO) > 0)
                payment.getInvoice().setStatus(InvoiceStatus.PARTIALLYPAID);
            else
                payment.getInvoice().setStatus(InvoiceStatus.FULLYPAID);
            account.setAccountStandingBalance(remainingBalance);
            payment = paymentRepo.save(payment);
            if(isAllowedToSetWarningToAccount(account, settings.getDebtsAllowed()))
                account.setStatus(AccountStatus.WARNING);
            else account.setStatus(AccountStatus.ACTIVE);
            account.setStatusUpdated(true);
            accountRepo.save(account);
        }
        return payment;
    }

    private BigDecimal calculatePenalty(DateTime dueDate, DateTime payDate, BigDecimal totalDue, BigDecimal penaltyRate){
        int dayDiff = new Period(dueDate, payDate).getDays();
        if(dayDiff > 0){
            System.out.println(dayDiff);
            BigDecimal penaltyPerDay = totalDue.multiply(penaltyRate);
            BigDecimal totalPenalty = new BigDecimal(dayDiff).multiply(penaltyPerDay), hundred = new BigDecimal(100);
            return totalPenalty.compareTo(hundred) > 0 ? hundred : totalPenalty;
        } else return BigDecimal.ZERO;
    }
    
    @Transactional(readOnly=true)
    @Override
    public Errors validate(PaymentForm paymentForm, Errors errors) {
        System.out.println("start validate");
        Account account = accountRepo.findOne(paymentForm.getAccountId());
        boolean create = paymentForm.getPayment().getId() == null;
        Payment payment = paymentForm.getPayment();
        if(account == null)
            errors.reject("global","Account does not exists");
        Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
        if(invoice == null)            
            errors.reject("global","No existing bill for this account");
        else{
            //find original payment if update
            Payment originalPayment = (create) ? null : paymentRepo.findOne(payment.getId());
            if(account.getAccountStandingBalance().doubleValue() == 0 && create)
                errors.reject("global", "This account has zero standing balance");
            else{
                //
                boolean validAmount =  (create) ? payment.getAmountPaid().compareTo(account.getAccountStandingBalance())
                        <=  0 : payment.getAmountPaid().compareTo(account.getAccountStandingBalance().add(originalPayment.getAmountPaid())) <= 0;
                if(!validAmount)
                    errors.rejectValue("payment.amountPaid","","Invalid amount");
            }
            if(payment.getAmountPaid().doubleValue() != 0){
                //not debt payment checking
                if(paymentForm.getPayment().getReceiptNumber().isEmpty()) {
                    errors.rejectValue("payment.receiptNumber", "", "");
                    errors.reject("global", "OR number is required unless DEBT payment");
                }
                else {
                    Payment paymentUniqueOr = paymentRepo.findByReceiptNumber(paymentForm.getPayment().getReceiptNumber());
                    if(paymentUniqueOr != null) {
                        if(create)
                            errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
                        else {
                            if(!paymentUniqueOr.getId().equals(originalPayment.getId()))
                                errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
                        }
                    }
                }
            } else {
                //debt payment checking
                if(!payment.getReceiptNumber().isEmpty()){
                    errors.rejectValue("payment.receiptNumber", "", "");
                    errors.reject("global", "OR number should be left empty for DEBT payment");
                }
                int paymentCount = invoice.getPayments().size();
                if( (create && paymentCount > 0) || (!create && paymentCount > 1) )
                    errors.reject("global", "DEBT payment not allowed unless FIRST PAYMENT of the month.");
            }
        }
        return errors;
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