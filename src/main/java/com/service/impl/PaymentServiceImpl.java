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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;


@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentRepository paymentRepo;    
    private InvoiceRepository invoiceRepo;
    private AccountRepository accountRepo;
    private SettingsService settingsService;
    private ScheduleRepository schedRepo;
    
    @Autowired 
    public PaymentServiceImpl(PaymentRepository paymentRepo, InvoiceRepository invoiceRepo, 
                              AccountRepository accountRepo, SettingsService settingsService,
                              ScheduleRepository schedRepo) {
        this.paymentRepo = paymentRepo;
        this.invoiceRepo = invoiceRepo;
        this.accountRepo = accountRepo;
        this.settingsService = settingsService;
        this.schedRepo = schedRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public JRDataSource paymentHistoryDataSource(List<Long> paymentIds) {
        List<Payment> report = new ArrayList<>();
        report.add(new Payment());
        for(Long id : paymentIds)
            report.add(paymentRepo.findById(id));
        return new JRBeanCollectionDataSource(report);
    }

    @Override
    @Transactional
    public Payment save(PaymentForm form){
        if(form.getPayment().getId() == null)
            return create(form);
        else return update(form);
    }

    private Payment create(PaymentForm form){
        Settings settings = settingsService.getCurrentSettings();
        Account account = accountRepo.findOne(form.getAccountId());
        Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
        BigDecimal oldBalance = BigDecimal.ZERO.add(account.getAccountStandingBalance());
        Payment payment = form.getPayment();
        int paymentYear = payment.getDate().getYear(),
                paymentMonth = payment.getDate().getMonthOfYear();
        if(paymentMonth == 1){
            paymentMonth = 12;
            paymentYear --;
        } else paymentMonth --;
        //setting payment information
        Schedule schedule = schedRepo.findByMonthAndYear(paymentMonth, paymentYear);
        if(schedule == null) {
            schedule = new Schedule(paymentMonth, paymentYear);
            schedule = schedRepo.save(schedule);
        }
        account.getPayments().add(payment);
        payment.setSchedule(schedule);
        payment.setInvoice(invoice);
        payment.setAccount(account);
        payment.setInvoiceTotal(oldBalance);
        invoice.getPayments().add(payment); //adding payment to invoice
        payment = paymentRepo.save(payment); //save
        // -- Updating invoice based on payment
        BigDecimal remainingBalance = oldBalance.subtract(payment.getAmountPaid());
        invoice = payment.getInvoice();
        invoice.setRemainingTotal(remainingBalance);
        if(remainingBalance.compareTo(BigDecimal.ZERO) == 0)
            invoice.setStatus(InvoiceStatus.FULLYPAID);
        else
            invoice.setStatus(InvoiceStatus.PARTIALLYPAID);
        invoiceRepo.save(invoice);
        //updating account based on payment
        if(isAllowedToSetWarningToAccount(account, settings.getDebtsAllowed()))
            account.setStatus(AccountStatus.WARNING);
        else account.setStatus(AccountStatus.ACTIVE);
        account.setAccountStandingBalance(remainingBalance);
        account.setStatusUpdated(true);
        accountRepo.save(account);
        return payment;
    }

    private Payment update(PaymentForm form){
        Payment originalPayment = paymentRepo.findOne(form.getPayment().getId());
        originalPayment.setVersion(form.getPayment().getVersion());
        originalPayment.setReceiptNumber(form.getPayment().getReceiptNumber());
        originalPayment = paymentRepo.save(originalPayment);
        return originalPayment;
    }

    @Override
    public BigDecimal calculatePenalty(Invoice invoice, BigDecimal penaltyRate){
        BigDecimal totalDue = invoice.getRemainingTotal(), penalty = totalDue.multiply(penaltyRate),
                hundred = new BigDecimal(100);
        return penalty.compareTo(hundred) > 0 ? hundred : penalty;
    }

    @Transactional
    @Override
    public BindingResult validate(PaymentForm form, BindingResult errors){
        BindException result = new BindException(errors);
        boolean paymentIsNew = form.getPayment().getId() == null;
        if(!result.hasErrors()){
            Account account = accountRepo.findOne(form.getAccountId());
            Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
            if(account == null)
                result.reject("global","Account does not exists");
            if(invoice == null)
                result.reject("global","No existing bill for this account");
            else if(paymentIsNew ){
                if(account.getAccountStandingBalance().doubleValue() == 0)
                    result.reject("global", "This account has zero standing balance");
                else {
                    boolean isLatePayment = form.getPayment().getDate().isAfter(invoice.getDueDate());
                    if(isLatePayment && invoice.getStatus().equals(InvoiceStatus.UNPAID) ){
                        result.reject("global", "Finalize payments to add penalty.");
                    }
                    if(form.getPayment().getAmountPaid().compareTo(account.getAccountStandingBalance()) > 0)
                        result.rejectValue("payment.amountPaid", "",  "Invalid amount");
                }
            }
            Payment paymentUniqueOr = paymentRepo.findByReceiptNumber(form.getPayment().getReceiptNumber());
            if(paymentUniqueOr != null){
                if(paymentIsNew)
                    errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
                else if(!paymentUniqueOr.getId().equals(form.getPayment().getId()))
                        errors.rejectValue("payment.receiptNumber", "", "OR number already exists");
            }
        }
        return result;
    }

    @Transactional(readOnly=true)
    @Override
    public boolean isAllowedToSetWarningToAccount(Account account, Integer debtsAllowed) {
        return invoiceRepo
                .findByAccountOrderByIdDesc(account, new PageRequest(0, debtsAllowed))
                .stream()
                .filter(e -> !e.getStatus().equals(InvoiceStatus.FULLYPAID))
                .count() == debtsAllowed;
    }

    @Transactional
    @Override
    public List<Account> updateAccountsWithNoPayments(Address address) {
        List<Account> accounts = accountRepo.findByAddressInAndStatusIn(Arrays.asList(address), Arrays.asList(AccountStatus.ACTIVE));
        Settings currentSettings = settingsService.getCurrentSettings();
        BigDecimal penaltyRate = new BigDecimal(currentSettings.getPenalty().toString());
        List<Account> updated = new ArrayList<>();
        for(Account account: accounts){
            Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
            if(invoice != null && !invoice.getStatus().equals(InvoiceStatus.FULLYPAID)){
                account = invoice.getAccount();
                if(invoice.getStatus().equals(InvoiceStatus.DEBT) || invoice.getStatus().equals(InvoiceStatus.UNPAID)){
                    if(invoice.getRemainingTotal().compareTo(BigDecimal.ZERO) > 0)
                        invoice.setStatus(InvoiceStatus.DEBT);
                    else invoice.setStatus(InvoiceStatus.FULLYPAID);
                    invoiceRepo.save(invoice);
                    account.setStatusUpdated(true);
                }
                if(invoice.getRemainingTotal().compareTo(BigDecimal.ZERO) > 0 && invoice.getPenalty().compareTo(BigDecimal.ZERO) == 0 && (invoice.getDueDate().isEqualNow() || invoice.getDueDate().isBeforeNow())){
                    BigDecimal penalty = calculatePenalty(invoice, penaltyRate);
                    invoice.setPenalty(penalty);
                    invoice.setRemainingTotal(invoice.getRemainingTotal().add(penalty));
                    invoice.setNetCharge(invoice.getNetCharge().add(penalty));
                    invoiceRepo.save(invoice);
                    account = invoice.getAccount();
                    account.setAccountStandingBalance(invoice.getRemainingTotal());
                }
                if(isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed())){
                    account.setStatus(AccountStatus.WARNING);
                    updated.add(account);
                }
                accountRepo.save(account);
            }
        }
        return updated;
    }

    @Transactional(readOnly = true)
    @Override
    public JRDataSource getPreviousPaymentsDataSource(Account account) {
        List<Payment> report = new ArrayList<>();
        report.add(new Payment());
        report.addAll(paymentRepo.findByAccountIdWithReading(account.getId()));
        return new JRBeanCollectionDataSource(report);
    }
}