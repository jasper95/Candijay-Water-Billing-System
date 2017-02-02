/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.DataTableDao;
import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.forms.AccountForm;
import com.forms.CustomerForm;
import com.service.CustomerManagementService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bert
 */

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {
    
    private CustomerRepository customerRepo;
    private AccountRepository accountRepo;
    private AddressRepository addressRepo;
    private DeviceRepository deviceRepo;
    
    @Autowired
    public CustomerManagementServiceImpl(CustomerRepository customerRepo, AccountRepository accountRepo, 
            AddressRepository addressRepo, DeviceRepository deviceRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.addressRepo = addressRepo;
        this.deviceRepo = deviceRepo;
    }
             
    @Override
    @Transactional
    public Customer updateCustomer(CustomerForm customerForm) {
        return customerRepo.save(customerForm.getCustomer());
    }   

    @Override
    @Transactional
    public Customer createCustomer(CustomerForm customerForm) {
        Customer customer = customerRepo.save(customerForm.getCustomer());
        saveNewAccount(customerForm.getAccount(), customerForm.getDevice(), customerForm.getAddress(), customer);
        return customer;
    }

    @Transactional
    @Override
    public Account createAccount(AccountForm accountForm) {
        return saveNewAccount(accountForm.getAccount(), accountForm.getDevice(), accountForm.getAddress(), customerRepo.findOne(accountForm.getCustomerId()));
    }

    private Account saveNewAccount(Account account, Device device, Address address, Customer customer){
        account.setCustomer(customer);
        String number = address.getAccountPrefix() + "-" + String.format("%05d", address.getAccountsCount());
        account.setNumber(number);
        account.setAddress(address);
        if(account.getId() == null)
            device.setStartDate(new Date());
        device.setActive(true);
        account = accountRepo.save(account);
        device.setOwner(account);
        deviceRepo.save(device);
        address.setAccountsCount(address.getAccountsCount()+1);
        addressRepo.save(address);
        return account;

    }

    @Transactional
    @Override
    public Account updateAccount(AccountForm accountForm){
        Address address = addressRepo.findByBrgy(accountForm.getAddress().getBrgy());
        Account account = accountRepo.findByNumber(accountForm.getAccount().getNumber());
        account.setAddress(address);
        account.setPurok(accountForm.getAccount().getPurok());
        return accountRepo.save(account);
    }

    @Transactional
    @Override
    public void changeAccountStatus(Account account, AccountStatus status) {
       account.setStatus(status);
       accountRepo.save(account);
    }
    
    @Transactional
    @Override
    public Device saveNewDevice(String accountNumber, Device device) {
        device.setOwner(accountRepo.findByNumber(accountNumber));
        return deviceRepo.save(device);
    }

    @Transactional
    @Override
    public Device updateDevice(Long id,  Device device){
        Device origDevice = deviceRepo.findOne(id);
        origDevice.setBrand(device.getBrand());
        origDevice.setMeterCode(device.getMeterCode());
        origDevice.setLastReading(device.getLastReading());
        return deviceRepo.save(origDevice);
    }

    @Transactional
    @Override
    public void activateDevice(Device device) {
        List<Device> devices =  deviceRepo.findByOwner(device.getOwner());
        for(Device d : devices){
            if(!d.getId().equals(device.getId()))
                d.setActive(false);
            else d.setActive(true);
            deviceRepo.save(d);
        }

    }
}
