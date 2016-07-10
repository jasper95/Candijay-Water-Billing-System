/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.InvoiceRepository;
import com.dao.springdatajpa.MeterReadingRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.domain.Account;
import com.domain.Invoice;
import com.domain.MeterReading;
import com.domain.Schedule;
import com.domain.enums.InvoiceStatus;
import com.forms.MeterReadingForm;
import com.service.InvoicingService;
import com.service.MeterReadingService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.DateFormatSymbols;
import org.springframework.validation.Errors;
/**
 *
 * @author Bert
 */
@Service("mrService")
public class MeterReadingServiceImpl implements MeterReadingService{

    @Autowired
    private MeterReadingRepository mrRepo;
    @Autowired
    private ScheduleRepository schedRepo;
    @Autowired 
    private AccountRepository accountRepo;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private InvoiceRepository invoiceRepo;
    
    @Transactional(readOnly=true)
    @Override
    public List<MeterReading> findmeterReadingForAccount(Account account) {
        List<MeterReading> recentReadings = new ArrayList();
        for(MeterReading reading: mrRepo.findTop3ByAccountOrderByIdDesc(account))    
            recentReadings.add(reading);
        return recentReadings;
    }
    
    @Override
    @Transactional(readOnly=true)
    public MeterReading findAccountLastMeterReading(Account account, int monthLag) {
        List<MeterReading> readings = findmeterReadingForAccount(account);
        if (!readings.isEmpty() && readings.size() >= monthLag){
            return readings.get(monthLag-1);
        }
        return null;
    }

    @Override
    @Transactional
    public MeterReading saveMeterReading(MeterReadingForm form) {
        Account account = accountRepo.findOne(form.getAccountId());
        MeterReading reading = form.getMeterReading();
        Schedule sched;
        if(form.getMeterReading().getSchedule().getId() != null)
            sched= schedRepo.findOne(form.getMeterReading().getSchedule().getId());
        else
            sched = schedRepo.save(form.getMeterReading().getSchedule());
        reading.setAccount(account);
        reading.setSchedule(sched);
        account.addMeterReading(reading);
        reading = mrRepo.save(reading);
        invoicingService.generateInvoiceMeterReading(reading);
        return reading;
    }

    @Override
    @Transactional(readOnly=true)
    public boolean isReadingPaid(MeterReading reading) {
        Invoice invoice = invoiceRepo.findByAccountAndSchedule(reading.getAccount(), reading.getSchedule());
        if(invoice == null){
            return false;
        } else 
            return !invoice.getStatus().equals(InvoiceStatus.UNPAID);
        
    }
    
    @Override
    public Errors validate(MeterReadingForm form, Errors errors) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}