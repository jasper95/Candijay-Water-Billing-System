/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 201244055
 */
public class InvoiceReport {
    private Long billId;
    private String customerName;
    private String scheduleMonth;  
    private String accountAddress;
    private Long accountId;
    private Integer readingCurrent;
    private Integer readingConsumption;
    private Integer readingPrevious;
    private BigDecimal billBasic;
    private BigDecimal billSysLoss;
    private BigDecimal billDepFund;
    private BigDecimal billOthers;
    private BigDecimal billArrears;
    private BigDecimal billPenalty;
    private BigDecimal billTotal;
    private BigDecimal billDiscount;
    private Date dueDate;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getScheduleMonth() {
        return scheduleMonth;
    }

    public void setScheduleMonth(String scheduleMonth) {
        this.scheduleMonth = scheduleMonth;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getReadingCurrent() {
        return readingCurrent;
    }

    public void setReadingCurrent(Integer readingCurrent) {
        this.readingCurrent = readingCurrent;
    }

    public Integer getReadingConsumption() {
        return readingConsumption;
    }

    public void setReadingConsumption(Integer readingConsumption) {
        this.readingConsumption = readingConsumption;
    }

    public Integer getReadingPrevious() {
        return readingPrevious;
    }

    public void setReadingPrevious(Integer readingPrevious) {
        this.readingPrevious = readingPrevious;
    }
    
    public BigDecimal getBillOthers() {
        return billOthers;
    }

    public void setBillOthers(BigDecimal billOthers) {
        this.billOthers = billOthers;
    }

    public BigDecimal getBillArrears() {
        return billArrears;
    }

    public void setBillArrears(BigDecimal billArrears) {
        this.billArrears = billArrears;
    }

    public BigDecimal getBillPenalty() {
        return billPenalty;
    }

    public void setBillPenalty(BigDecimal billPenalty) {
        this.billPenalty = billPenalty;
    }

    public BigDecimal getBillBasic() {
        return billBasic;
    }

    public void setBillBasic(BigDecimal billBasic) {
        this.billBasic = billBasic;
    }

    public BigDecimal getBillSysLoss() {
        return billSysLoss;
    }

    public void setBillSysLoss(BigDecimal billSysLoss) {
        this.billSysLoss = billSysLoss;
    }

    public BigDecimal getBillDepFund() {
        return billDepFund;
    }

    public void setBillDepFund(BigDecimal billDepFund) {
        this.billDepFund = billDepFund;
    }

    public BigDecimal getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
    }

    public BigDecimal getBillDiscount() {
        return billDiscount;
    }

    public void setBillDiscount(BigDecimal billDiscount) {
        this.billDiscount = billDiscount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
}
