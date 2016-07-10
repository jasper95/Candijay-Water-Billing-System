/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.Invoice;
import com.domain.Payment;
import com.forms.PaymentForm;
import org.springframework.validation.Errors;

/**
 *
 * @author 201244055
 */
public interface PaymentService {
    //public Invoice findInvoiceById(Long id);
    public Payment save(PaymentForm form);
    public Payment updateAccountFromPayment(Payment payment);
    public Invoice findLatestBill(Account account);
    public Errors validate(PaymentForm form, Errors errors);
    public Payment findPaymentById(Long id);
    public boolean canEdit(Payment payment);
    public boolean isAllowedToSetWarningToAccount(Account account);
}