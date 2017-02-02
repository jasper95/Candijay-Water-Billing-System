/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;
import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.InvoiceStatus;
import com.forms.BillDiscountForm;
import com.service.InvoicingService;

import java.math.BigDecimal;

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
                    basic = new BigDecimal(settings.getBasicMinimum()),
                    total, systemLoss = new BigDecimal(settings.getSystemLossMinimum()),
                    depreciationFund = new BigDecimal(settings.getDepreciationFundMinimum());
        if(reading.getConsumption() > 5){
            BigDecimal adder = new BigDecimal(settings.getBasicRate());
            for(int i= 6; i <= reading.getConsumption(); i++){
                basic = basic.add(adder);
                if(i%10 == 0)
                    adder = adder.add(BigDecimal.ONE);
            }
            systemLoss = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getSystemLossRate()));
            depreciationFund = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getDepreciationFundRate()));
        }
        int month = reading.getSchedule().getMonth()+1;
        int year = reading.getSchedule().getYear();
        if(month > 12){
            month = 1;
            year++;
        }
        DateTime dueDate = new DateTime(year,
                                        month,
                                        reading.getAccount().getAddress().getDueDay(),
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
            newInvoice.setArrears(reading.getAccount().getAccountStandingBalance());
            newInvoice.setPenalty(BigDecimal.ZERO);
            newInvoice.setOthers(others);
            reading.setInvoice(newInvoice);
            newInvoice.setReading(reading);
            total = total.add(reading.getAccount().getAccountStandingBalance());
            newInvoice.setStatus(InvoiceStatus.UNPAID);
        } else total = total.add(newInvoice.getArrears().add(newInvoice.getPenalty()));
        newInvoice.setNetCharge(total);
        newInvoice.setRemainingTotal(total);
        newInvoice = invoiceRepo.save(newInvoice);
        reading.getAccount().setAccountStandingBalance(total);
        if(newInvoice.getStatus().equals(InvoiceStatus.UNPAID))
            reading.getAccount().setStatusUpdated(false);
        accountRepo.save(reading.getAccount());
    }

    @Transactional
    @Override
    public Invoice updateDiscount(BillDiscountForm form) {
        Invoice invoice = invoiceRepo.findOne(form.getBillId());
        BigDecimal oldDiscount = invoice.getDiscount();
        invoice.setDiscount(form.getDiscount());
        invoice.setNetCharge((invoice.getNetCharge().add(oldDiscount)).subtract(form.getDiscount()));
        Account account = invoice.getAccount();
        invoice.setRemainingTotal((invoice.getRemainingTotal().add(oldDiscount)).subtract(form.getDiscount()));
        account.setAccountStandingBalance(invoice.getRemainingTotal());
        accountRepo.save(account);
        return invoiceRepo.save(invoice);
    }
}
