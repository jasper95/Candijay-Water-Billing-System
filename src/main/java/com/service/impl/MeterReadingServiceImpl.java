/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.domain.enums.InvoiceStatus;
import com.forms.MeterReadingForm;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.service.InvoicingService;
import com.service.MeterReadingService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AddressRepository addressRepo;

    @Autowired
    public MeterReadingServiceImpl(InvoiceRepository invoiceRepo, InvoicingService invoicingService, AccountRepository accountRepo, ScheduleRepository schedRepo,
                                   MeterReadingRepository mrRepo, DeviceRepository deviceRepo, ModifiedReadingRepository modifiedReadingRepo,
                                   AddressRepository addressRepo){
        this.mrRepo = mrRepo;
        this.schedRepo = schedRepo;
        this.accountRepo = accountRepo;
        this.invoicingService = invoicingService;
        this.invoiceRepo = invoiceRepo;
        this.deviceRepo = deviceRepo;
        this.modifiedReadingRepo = modifiedReadingRepo;
        this.addressRepo = addressRepo;
    }

    @Override
    @Transactional(readOnly=true)
    public MeterReading findAccountLastMeterReading(Account account) {
        return mrRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
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
        Invoice invoice = reading.getInvoice();
        return (!invoice.getStatus().equals(InvoiceStatus.UNPAID) && !invoice.getStatus().equals(InvoiceStatus.DEBT));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isDoneReadingAddressIn(Collection<Address> addresses) throws Exception {
        Calendar c = Calendar.getInstance();
        Integer currentYear = c.get(Calendar.YEAR);
        Integer currentMonth = c.get(Calendar.MONTH)+1;
        if(currentMonth == 1){
            currentMonth = 12;
            currentYear--;
        } else currentMonth --;
        Schedule schedule = schedRepo.findByMonthAndYear(currentMonth, currentYear);
        if(schedule == null)
            throw new Exception("No meter reading data for previous month");
        Long meterReadingCount = mrRepo.countByScheduleAndAccount_AddressIn(schedule, addresses);
        Long accountsCount = accountRepo.countByAddressInAndStatus(addresses, AccountStatus.ACTIVE);
        return meterReadingCount >= accountsCount;
    }

    @Transactional
    @Override
    public boolean deleteReading(Long id) {
        MeterReading reading = mrRepo.findOne(id), lastMeterReading = mrRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(reading.getAccount());
        if(reading != null && reading.getInvoice().getStatus().equals(InvoiceStatus.UNPAID) && reading.equals(lastMeterReading)){
            Device device = deviceRepo.findByOwnerAndActive(reading.getAccount(), true);
            Account account = reading.getAccount();
            account.setStatusUpdated(true);
            device.setLastReading(device.getLastReading()-reading.getConsumption());
            account.setPenalty(reading.getInvoice().getPenalty());
            account.setAccountStandingBalance(account.getAccountStandingBalance().subtract(reading.getInvoice().getNetCharge()));
            mrRepo.delete(reading);
            deviceRepo.save(device);
            accountRepo.save(account);
            return true;
        } else return false;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> findAccountsWithNoLatestReading(List<Address> addresses) throws Exception{
        Calendar c = Calendar.getInstance();
        Integer currentYear = c.get(Calendar.YEAR);
        Integer currentMonth = c.get(Calendar.MONTH)+1;
        if(currentMonth == 1){
            currentMonth = 12;
            currentYear--;
        } else currentMonth --;
        Schedule schedule = schedRepo.findByMonthAndYear(currentMonth, currentYear);
        if(schedule == null)
            throw new Exception("No meter reading data for previous month");
        List<MeterReading> ms = mrRepo.findByScheduleAndAccount_AddressIn(schedule, addresses);
        List<Account> accounts = accountRepo.findByAddressInAndStatusIn(addresses, Collections.singletonList(AccountStatus.ACTIVE)), accountsNoReading = new ArrayList();
        List<String> num = new ArrayList<>();
        for(MeterReading reading : ms)
            num.add(reading.getAccount().getNumber());
        for(Account account : accounts){
            if(!num.contains(account.getNumber())){
                accountsNoReading.add(account);
            }
        }
        return accountsNoReading;
    }

    @Transactional(readOnly = true)
    @Override
    public DataSet<Account> findAccountsWithCustomParams(DatatablesCriterias criterias) {
        String isBrgy= "", brgy = "", zone = "";
        for(ColumnDef columnDef: criterias.getColumnDefs()){
            System.out.println(columnDef.getName());
            if(columnDef.getName().equals("customer.firstName"))
                isBrgy = columnDef.getSearch();
            else if(columnDef.getName().equals("number"))
                brgy = columnDef.getSearch();
            else if(columnDef.getName().equals("customer.lastname"))
                zone = columnDef.getSearch();
        }
        try{
            List<Address> addresses = new ArrayList<>();
            if(isBrgy.equals("1"))
                addresses.add(addressRepo.findByBrgy(brgy));
            else
                addresses = addressRepo.findByLocationCode(Integer.valueOf(zone));
            List<Account> results = findAccountsWithNoLatestReading(addresses);
            Long filteredCount = (long) results.size();
            int offset = (results.size() > criterias.getStart() +criterias.getLength()) ?
                    criterias.getStart() +criterias.getLength() : results.size();
            results = results.subList(criterias.getStart(), offset);
            return new DataSet<>(results, accountRepo.count(), filteredCount);
        }catch (Exception e){
            return new DataSet<>(new ArrayList<>(), accountRepo.count(), (long) 0);
        }

    }
}