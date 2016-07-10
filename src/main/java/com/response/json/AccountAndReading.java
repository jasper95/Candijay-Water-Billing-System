/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.response.json;

import com.domain.Account;
import com.domain.MeterReading;
import java.util.List;

/**
 *
 * @author Bert
 */
public class AccountAndReading implements java.io.Serializable {
    private Account account;
    private List<MeterReading> readings;

    public AccountAndReading(Account account, List<MeterReading> readings) {
        this.account = account;
        this.readings = readings;
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<MeterReading> getReadings() {
        return readings;
    }

    public void setReadings(List<MeterReading> readings) {
        this.readings = readings;
    }
    
}
