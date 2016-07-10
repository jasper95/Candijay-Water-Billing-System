/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import com.domain.MeterReading;
import com.domain.Schedule;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Bert
 */
public class MeterReadingForm implements java.io.Serializable{
    @Valid
    private MeterReading meterReading = null;
    @NotNull
    private Long accountId = null;
    
    public MeterReading getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(MeterReading meterReading) {
        this.meterReading = meterReading;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
