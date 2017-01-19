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
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.service.PaymentService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.service.SettingsService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

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
        Schedule sched = schedRepo.findByMonthAndYear(paymentMonth, paymentYear);
        account.getPayments().add(payment);
        payment.setSchedule(sched);
        payment.setInvoice(invoice);
        payment.setAccount(account);
        payment.setInvoiceTotal(oldBalance);
        invoice.getPayments().add(payment); //adding payment to invoice
        payment = paymentRepo.save(payment); //save
        // -- Updating invoice based on payment
        BigDecimal remainingBalance = oldBalance.subtract(payment.getAmountPaid());
        invoice = payment.getInvoice();
        if(remainingBalance.compareTo(invoice.getNetCharge()) == 0)
            invoice.setStatus(InvoiceStatus.DEBT);
        else if(remainingBalance.compareTo(BigDecimal.ZERO) > 0)
            invoice.setStatus(InvoiceStatus.PARTIALLYPAID);
        else
            invoice.setStatus(InvoiceStatus.FULLYPAID);
        invoice.setRemainingTotal(remainingBalance);
        invoiceRepo.save(invoice);
        //updating account based on payment
        if(isAllowedToSetWarningToAccount(account, settings.getDebtsAllowed()))
            account.setStatus(AccountStatus.WARNING);
        else account.setStatus(AccountStatus.ACTIVE);
        if(!isAlreadyPenalizedExcludingThisPayment(payment)) //is invoice already penalized
            account.setPenalty(calculatePenalty(payment, new BigDecimal(settings.getPenalty())));
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

    private static boolean isLatePayment(Payment payment){
        return new Period(payment.getInvoice().getDueDate(), payment.getDate()).getDays() > 0;
    }

    private boolean isAlreadyPenalizedExcludingThisPayment(Payment payment){
        Invoice invoice = payment.getInvoice();
        return invoice.getPayments().stream()
                            .filter(PaymentServiceImpl::isLatePayment)
                            .filter(e -> !e.equals(payment))
                            .findFirst()
                            .isPresent();
    }

    private BigDecimal calculatePenalty(Payment payment, BigDecimal penaltyRate){
        int dayDiff = new Period(payment.getInvoice().getDueDate(), payment.getDate()).getDays();
        if(dayDiff > 0){
            BigDecimal totalDue = payment.getInvoice().getNetCharge(), penalty = totalDue.multiply(penaltyRate),
                    hundred = new BigDecimal(100);
            return penalty.compareTo(hundred) > 0 ? hundred : penalty;
        } else return BigDecimal.ZERO;
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
            else if(paymentIsNew && account.getAccountStandingBalance().doubleValue() == 0)
                    result.reject("global", "This account has zero standing balance");
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

    /*@Transactional(readOnly=true)
    @Override
    public Errors validate(PaymentForm paymentForm, Errors errors) {
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
    }*/

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
        List<Account> accounts = accountRepo.findByAddressInAndStatusUpdatedAndStatusIn(Arrays.asList(address), false, Arrays.asList(AccountStatus.ACTIVE));
        Settings currentSettings = settingsService.getCurrentSettings();
        List<Account> updated = new ArrayList<>();
        for(Account account: accounts){
            Invoice invoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
            if(invoice != null){
                invoice.setStatus(InvoiceStatus.DEBT);
                account = invoice.getAccount();
                if(isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed()))
                    account.setStatus(AccountStatus.WARNING);
                account.setStatusUpdated(true);
                updated.add(accountRepo.save(account));
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