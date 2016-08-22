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
import java.util.List;

import com.service.SettingsService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

/**
 *
 * @author 201244055
 */
@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepository paymentRepo;    
    private InvoiceRepository invoiceRepo;
    private AccountRepository accountRepo;
    private SettingsService settingsService;
    private DeviceRepository deviceRepo;
    private ModifiedPaymentRepository modifiedPaymentRepo;
    
    @Autowired 
    public PaymentServiceImpl(PaymentRepository paymentRepo, InvoiceRepository invoiceRepo, 
                              AccountRepository accountRepo, SettingsService settingsService, DeviceRepository deviceRepo,
                              ModifiedPaymentRepository modifiedPaymentRepo) {
        this.paymentRepo = paymentRepo;
        this.invoiceRepo = invoiceRepo;
        this.accountRepo = accountRepo;
        this.settingsService = settingsService;
        this.deviceRepo = deviceRepo;
        this.modifiedPaymentRepo = modifiedPaymentRepo;
    }

    @Override
    public JRDataSource paymentHistoryDataSource(List<Long> paymentIds) {
        List<Payment> report = new ArrayList();
        report.add(new Payment());
        for(Long id : paymentIds)
            report.add(paymentRepo.findOne(id));
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
            oldPayment.setDiscount(form.getPayment().getDiscount());
            oldPayment.setVersion(form.getPayment().getVersion());
            oldPayment.setReceiptNumber(form.getPayment().getReceiptNumber());
            form.setPayment(oldPayment);
        }
        Account account = accountRepo.findOne(form.getAccountId());
        Invoice invoice = invoiceRepo.findTopByAccountOrderByIdDesc(account);
        Payment payment = form.getPayment();
        account.getPayments().add(payment);
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
        BigDecimal diff = payment.getInvoice().getNetCharge().subtract(payment.getAmountPaid().add(payment.getDiscount()));
        payment.getAccount().setAccountStandingBalance(diff);
        Settings settings = settingsService.getCurrentSettings();
        if(diff.doubleValue() > 0)
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
        payment.getAccount().setStatus(AccountStatus.ACTIVE);
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
                    paymentForm.getPayment().getDiscount().doubleValue() >= 0 &&
                    (paymentForm.getPayment().getAmountPaid().add(paymentForm.getPayment().getDiscount()).doubleValue() 
                        <= invoice.getNetCharge().doubleValue());
            if(!invoice.getStatus().equals(InvoiceStatus.UNPAID) && paymentForm.getPayment().getId() == null)
                errors.reject("global", "No existing unpaid bill for this account");
            if(!validAmount){
                errors.rejectValue("payment.amountPaid","","");
                errors.rejectValue("payment.discount","","");
                errors.reject("global", "Invalid amount paid and discount.");
            }
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
        return errors;
    }

    @Transactional(readOnly=true)
    @Override
    public Payment findPaymentById(Long id) {
        return paymentRepo.findOne(id);
    }

    @Transactional(readOnly=true)
    @Override
    public boolean isAllowedToSetWarningToAccount(Account account, Integer debtsAllowed) {
        int ctr = 0;
        for(Invoice invoice: invoiceRepo.findTop5ByAccountOrderByIdDesc(account)){
            if(!invoice.getStatus().equals(InvoiceStatus.UNPAID))
                break;
            else ctr++;

        }
        return debtsAllowed.compareTo(ctr) <= 0;
    }

    @Transactional
    @Override
    public List<Account> updateAccountsWithNoPayments(Address address) {
        List<Account> updated = new ArrayList<Account>();
        Settings currentSettings = settingsService.getCurrentSettings();
        for(Account account: accountRepo.findByAddressAndStatus(address, AccountStatus.ACTIVE)){
            account.setStatusUpdated(true);
            account.setPenalty(BigDecimal.ZERO);
            if(isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed()))
                account.setStatus(AccountStatus.WARNING);
            updated.add(accountRepo.save(account));
        }
        return updated;
    }
}