/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.*;
import com.domain.*;
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

    private MeterReadingRepository mrRepo;
    private ScheduleRepository schedRepo;
    private AccountRepository accountRepo;
    private InvoicingService invoicingService;
    private InvoiceRepository invoiceRepo;
    private DeviceRepository deviceRepo;
    private ModifiedReadingRepository modifiedReadingRepo;

    @Autowired
    public MeterReadingServiceImpl(InvoiceRepository invoiceRepo, InvoicingService invoicingService, AccountRepository accountRepo, ScheduleRepository schedRepo,
                                   MeterReadingRepository mrRepo, DeviceRepository deviceRepo, ModifiedReadingRepository modifiedReadingRepo){
        this.mrRepo = mrRepo;
        this.schedRepo = schedRepo;
        this.accountRepo = accountRepo;
        this.invoicingService = invoicingService;
        this.invoiceRepo = invoiceRepo;
        this.deviceRepo = deviceRepo;
        this.modifiedReadingRepo = modifiedReadingRepo;
    }
    
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
        boolean updateFlag = false;
        ModifiedReading modifiedReading = null;
        Long oldVersion = reading.getVersion();
        if(reading.getId() != null){
            modifiedReading = new ModifiedReading(mrRepo.findOne(reading.getId()));
            updateFlag = true;
        }
        Schedule sched;
        if(form.getMeterReading().getSchedule().getId() != null)
            sched= schedRepo.findOne(form.getMeterReading().getSchedule().getId());
        else
            sched = schedRepo.save(form.getMeterReading().getSchedule());
        reading.setAccount(account);
        reading.setSchedule(sched);
        account.addMeterReading(reading);
        reading = mrRepo.saveAndFlush(reading);
        if(updateFlag && reading.getVersion().compareTo(oldVersion) > 0) {
            modifiedReading.setReading(reading);
            modifiedReadingRepo.save(modifiedReading);
        }
        invoicingService.generateInvoiceMeterReading(reading);
        Device device = deviceRepo.findByOwnerAndActive(account, true);
        device.setLastReading(reading.getReadingValue());
        deviceRepo.save(device);
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