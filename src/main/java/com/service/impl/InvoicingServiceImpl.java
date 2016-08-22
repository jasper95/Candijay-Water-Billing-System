/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;
import com.charts.ChartData;
import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.InvoiceStatus;
import com.service.InvoicingService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.util.*;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bert
 */
@Service("invoicingService")
public class InvoicingServiceImpl implements InvoicingService {

    private InvoiceRepository invoiceRepo;
    private SettingsRepository settingsRepo;
    private AccountRepository accountRepo;

    @Autowired
    public InvoicingServiceImpl(InvoiceRepository invoiceRepo, MeterReadingRepository mrRepo, PaymentRepository paymentRepo,
                                SettingsRepository settingsRepo, AccountRepository accountRepo, ScheduleRepository schedRepo, ExpenseRepository expenseRepo){

        this.invoiceRepo = invoiceRepo;
        this.settingsRepo = settingsRepo;
        this.accountRepo = accountRepo;
    }
    
    @Override
    public void generateInvoiceMeterReading(MeterReading reading) {
        Settings settings = settingsRepo.findAll().get(0);
        BigDecimal others = new BigDecimal(settings.getPes()), 
                    basic = new BigDecimal( reading.getConsumption()).multiply(new BigDecimal(settings.getBasic())),
                    total, systemLoss = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getSystemLoss())), 
                    depreciationFund = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getDepreciationFund())); 
        int month = reading.getSchedule().getMonth()+1;
        int year = reading.getSchedule().getYear();
        if(month > 12){
            month = 1;
            year++;
        }
        DateTime dueDate = new DateTime(year,
                                        month,
                                        reading.getAccount().getAddress().getAddressGroup().getDueDay(),
                                        0, 0);
        Invoice newInvoice = (reading.getInvoice() == null) ? new Invoice() : reading.getInvoice();
        newInvoice.setSchedule(reading.getSchedule());
        newInvoice.setDueDate(dueDate);
        newInvoice.setBasic(basic);
        newInvoice.setDepreciationFund(depreciationFund);
        newInvoice.setSystemLoss(systemLoss);
        total = basic.add(systemLoss.add(depreciationFund.add(others)));
        if(newInvoice.getId() == null){
            newInvoice.setAccount(reading.getAccount());
            newInvoice.setStatus(InvoiceStatus.UNPAID);
            newInvoice.setArrears(reading.getAccount().getAccountStandingBalance());
            newInvoice.setPenalty(reading.getAccount().getPenalty());
            newInvoice.setOthers(others);
            reading.setInvoice(newInvoice);
            newInvoice.setReading(reading);
            total = total.add(reading.getAccount().getAccountStandingBalance().add(reading.getAccount().getPenalty()));
        } else total = total.add(newInvoice.getArrears().add(newInvoice.getPenalty()));
        newInvoice.setNetCharge(total);
        invoiceRepo.save(newInvoice);
        reading.getAccount().setAccountStandingBalance(total);
        reading.getAccount().setStatusUpdated(false);
        accountRepo.save(reading.getAccount());
    }
}
