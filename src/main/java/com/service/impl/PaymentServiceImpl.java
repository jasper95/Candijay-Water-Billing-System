/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.DeviceRepository;
import com.dao.springdatajpa.InvoiceRepository;
import com.dao.springdatajpa.PaymentRepository;
import com.dao.springdatajpa.SettingsRepository;
import com.domain.Account;
import com.domain.Device;
import com.domain.Invoice;
import com.domain.Payment;
import com.domain.Settings;
import com.domain.enums.AccountStatus;
import com.domain.enums.InvoiceStatus;
import com.forms.PaymentForm;
import com.service.PaymentService;
import java.math.BigDecimal;
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
    private SettingsRepository settingsRepo;
    private DeviceRepository deviceRepo;
    
    @Autowired 
    public PaymentServiceImpl(PaymentRepository paymentRepo, InvoiceRepository invoiceRepo, 
            AccountRepository accountRepo, SettingsRepository settingsRepo, DeviceRepository deviceRepo) {
        this.paymentRepo = paymentRepo;
        this.invoiceRepo = invoiceRepo;
        this.accountRepo = accountRepo;
        this.settingsRepo = settingsRepo;
        this.deviceRepo = deviceRepo;
    }
        
    @Override
    @Transactional
    public Payment save(PaymentForm form) {
        Account account = accountRepo.findOne(form.getAccountId());
        Invoice invoice = findLatestBill(account);
        Payment payment = form.getPayment();
        account.getPayments().add(payment);
        payment.setInvoice(invoice);
        payment.setAccount(account);
        invoice.setPayment(payment);
        payment = paymentRepo.save(payment);
        payment = updateAccountFromPayment(payment);
        return payment;
    }

    @Override
    public Payment updateAccountFromPayment(Payment payment) {
        BigDecimal diff = payment.getInvoice().getNetCharge().subtract(payment.getAmountPaid().add(payment.getDiscount()));
        payment.getInvoice().getAccount().setAccountStandingBalance(diff);
        Settings settings = settingsRepo.findAll().get(0);
        if(diff.compareTo(payment.getInvoice().getNetCharge()) == 0){
            payment.getInvoice().setStatus(InvoiceStatus.DEBT);
        }
        else if(diff.doubleValue() > 0){
            payment.getInvoice().setStatus(InvoiceStatus.PARTIALLYPAID);
        }
        else {
            payment.getInvoice().setStatus(InvoiceStatus.FULLYPAID);
        }
        int dayDiff = new Period(payment.getInvoice().getDueDate(), payment.getDate()).getDays();
        if(dayDiff > 0 ){
            payment.getAccount().setPenalty(new BigDecimal(dayDiff).multiply(new BigDecimal(settings.getPenalty())));
            if(payment.getAccount().getPenalty().compareTo(new BigDecimal(100)) > 0)
                payment.getAccount().setPenalty(new BigDecimal(100));
        } else {
            payment.getAccount().setPenalty(BigDecimal.ZERO);
        }
        payment = paymentRepo.save(payment);
        if(isAllowedToSetWarningToAccount(payment.getAccount()))
            payment.getAccount().setStatus(AccountStatus.WARNING);
        else payment.getAccount().setStatus(AccountStatus.ACTIVE);
        accountRepo.save(payment.getAccount());
        Device device = deviceRepo.findByOwnerAndActive(payment.getAccount(), true);
        device.setLastReading(payment.getInvoice().getReading().getReadingValue());
        deviceRepo.save(device);
        return payment;
    }
    
    @Transactional(readOnly=true)
    @Override
    public Invoice findLatestBill(Account account) {
        return invoiceRepo.findTopByAccountOrderByIdDesc(account);
    }    
    
    @Transactional(readOnly=true)
    @Override
    public Errors validate(PaymentForm paymentForm, Errors errors) {
        Account account = accountRepo.findOne(paymentForm.getAccountId());
        if(account == null)
            errors.reject("Account does not exists");
        Invoice invoice = findLatestBill(account);
        if(invoice == null)            
            errors.reject("No existing bill for this account");       
        else{
            //Payment payment = (paymentForm.getPayment().getId() == null) ? paymentForm.getPayment() : paymentRepo.findOne(paymentForm.getPayment().getId());
            boolean validAmount = (paymentForm.getPayment().getAmountPaid().doubleValue() >= 0) &&
                    paymentForm.getPayment().getDiscount().doubleValue() >= 0 &&
                    (paymentForm.getPayment().getAmountPaid().add(paymentForm.getPayment().getDiscount()).doubleValue() 
                        <= invoice.getNetCharge().doubleValue());
            if(!invoice.getStatus().equals(InvoiceStatus.UNPAID) && paymentForm.getPayment().getId() == null)
                errors.reject("You can only pay unpaid invoice");
            if(!validAmount){
                errors.reject("Invalid amount paid and discount.");
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
    public boolean canEdit(Payment payment) {
        return payment.getInvoice().getId().equals(invoiceRepo.findTopByAccountOrderByIdDesc(payment.getAccount()).getId());
    }
    
    @Transactional(readOnly=true)
    @Override
    public boolean isAllowedToSetWarningToAccount(Account account) {
        int ctr = 0;
        for(Payment payment: paymentRepo.findTop3ByAccountOrderByIdDesc(account)){
            if(payment.getInvoice().getStatus().equals(InvoiceStatus.DEBT))
                ctr++;
        }
        return ctr == 3;
    }
}