/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.Address;
import com.domain.MeterReading;
import com.forms.MeterReadingForm;

import java.util.Collection;
import java.util.List;
import org.springframework.validation.Errors;

/**
 *  interface created for services used in meter reading module
 * @author Bert
 */
public interface MeterReadingService {
    MeterReading findAccountLastMeterReading(Account account, int monthLag);
    MeterReading saveMeterReading(MeterReadingForm form);
    boolean isReadingPaid(MeterReading reading);
    boolean isDoneReadingAddressIn(Collection<Address> addresses) throws Exception;
}