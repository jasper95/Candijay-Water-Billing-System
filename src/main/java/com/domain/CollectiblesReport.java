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
public class CollectiblesReport {
    private Long billId;
    private Long accountNo;
    private BigDecimal amount;
    private BigDecimal basic;
    private BigDecimal arrears;
    private BigDecimal sysLoss;
    private BigDecimal depFund;
    private BigDecimal pes;
    private BigDecimal penalty;
    private Integer locationCode;
    private String firstName;
    private String lastName;
    private Integer members;
    
    
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

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

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public BigDecimal getBasic() {
        return basic;
    }

    public void setBasic(BigDecimal basic) {
        this.basic = basic;
    }

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
    }

    public BigDecimal getSysLoss() {
        return sysLoss;
    }

    public void setSysLoss(BigDecimal sysLoss) {
        this.sysLoss = sysLoss;
    }

    public BigDecimal getDepFund() {
        return depFund;
    }

    public void setDepFund(BigDecimal depFund) {
        this.depFund = depFund;
    }

    public BigDecimal getPes() {
        return pes;
    }

    public void setPes(BigDecimal pes) {
        this.pes = pes;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    
}
