/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain;

import java.math.BigDecimal;

/**
 *
 * @author Bert
 */
public class CollectionReport {
    private Long accountNo;
    private BigDecimal amount;
    private Integer locationCode;
    private String firstName;
    private String lastName;
    private BigDecimal discount;
    private Long orNumber;
    private BigDecimal due;

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Integer locationCode) {
        this.locationCode = locationCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getOrNumber() {
        return orNumber;
    }

    public void setOrNumber(Long orNumber) {
        this.orNumber = orNumber;
    }

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }
  
    
}
