/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import com.domain.Payment;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author 201244055
 */
public class PaymentForm {
    
    @NotNull
    private Long accountId;
    @Valid
    private Payment payment;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
}
