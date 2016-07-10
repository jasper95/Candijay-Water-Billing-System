/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.MeterReading;
import com.forms.MeterReadingForm;
import java.util.List;
import org.springframework.validation.Errors;

/**
 *
 * @author Bert
 */
public interface MeterReadingService {
    public List<MeterReading> findmeterReadingForAccount(Account account);
    public MeterReading findAccountLastMeterReading(Account account, int monthLag);
    public MeterReading saveMeterReading(MeterReadingForm form);
    public boolean isReadingPaid(MeterReading reading);
    public Errors validate(MeterReadingForm form, Errors errors);
}