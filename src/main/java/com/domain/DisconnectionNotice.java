/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain;

import java.util.Date;

/**
 *
 * @author Bert
 */
public class DisconnectionNotice {
    private Long accountId;
    private String accountNumber;
    private String customerName;
    private String address;
    private String balanceWords;
    private String balanceNumbers;
    private Date disconnectionDate;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalanceWords() {
        return balanceWords;
    }

    public void setBalanceWords(String balanceWords) {
        this.balanceWords = balanceWords;
    }

    public String getBalanceNumbers() {
        return balanceNumbers;
    }

    public void setBalanceNumbers(String balanceNumbers) {
        this.balanceNumbers = balanceNumbers;
    }

    public Date getDisconnectionDate() {
        return disconnectionDate;
    }

    public void setDisconnectionDate(Date disconnectionDate) {
        this.disconnectionDate = disconnectionDate;
    }
    
    
}
