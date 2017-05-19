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

import com.service.SettingsService;
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
    private SettingsService settingsService;
    private AccountRepository accountRepo;

    @Autowired
    public InvoicingServiceImpl(InvoiceRepository invoiceRepo, SettingsService settingsService, AccountRepository accountRepo){
        this.invoiceRepo = invoiceRepo;
        this.settingsService = settingsService;
        this.accountRepo = accountRepo;
    }
    
    private BigDecimal computeBasic(Integer consumption, BigDecimal basic, BigDecimal basicRate){
        for(int i= 6; i <= consumption; i++){
            basic = basic.add(basicRate);
            if(i%10 == 0)
                basicRate = basicRate.add(BigDecimal.ONE);
            if(i == 100)
                break;
        }
        if(consumption >= 100){
            BigDecimal moreThanHundred = new BigDecimal(consumption-100);
            basic = basic.add(basicRate.multiply(moreThanHundred));
        }
        return basic;
    }

    @Override
    public void generateInvoiceMeterReading(MeterReading reading) {
        Settings settings = settingsService.getCurrentSettings();
        BigDecimal others = new BigDecimal(settings.getPes().toString()),
                    basic = new BigDecimal(settings.getBasicMinimum().toString()),
                    total, systemLoss = new BigDecimal(settings.getSystemLossMinimum().toString()),
                    depreciationFund = new BigDecimal(settings.getDepreciationFundMinimum().toString());
        if(reading.getConsumption() > 5){
            basic = computeBasic(reading.getConsumption(), basic, new BigDecimal(settings.getBasicRate().toString()));
            systemLoss = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getSystemLossRate().toString()));
            depreciationFund = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getDepreciationFundRate().toString()));
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
        if(newInvoice.getStatus().equals(InvoiceStatus.UNPAID) || newInvoice.getStatus().equals(InvoiceStatus.DEBT)){
            newInvoice.setNetCharge(total);
            newInvoice.setRemainingTotal(total);
        } else {
            BigDecimal paidAmount = newInvoice.getPayments().stream()
                                                            .map(Payment::getAmountPaid)
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal newRemainingTotal = total.subtract(paidAmount);
            if(newRemainingTotal.compareTo(BigDecimal.ZERO) < 0)
                newRemainingTotal = BigDecimal.ZERO;
            newInvoice.setNetCharge(total);
            newInvoice.setRemainingTotal(newRemainingTotal);
        }
        newInvoice = invoiceRepo.save(newInvoice);
        reading.getAccount().setAccountStandingBalance(newInvoice.getRemainingTotal());
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
