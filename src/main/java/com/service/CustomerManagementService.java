/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.Customer;
import com.forms.AccountForm;
import com.forms.CustomerForm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.domain.Address;
import com.domain.Device;
import com.domain.enums.AccountStatus;


/**
 *
 * @author Bert
 */
public interface CustomerManagementService {
    public Customer updateCustomer(CustomerForm customerForm);
    public Customer createCustomer(CustomerForm customerForm);
    public Device saveNewDevice(String accountNumber, Device device);
    public Device updateDevice(Long id, Device device);
    public Account createAccount(AccountForm accountForm);
    public Account updateAccount(AccountForm accountForm);
    public void changeAccountStatus(Account account, AccountStatus status);
    public void activateDevice(Device device);
    public HashMap<String,Collections> getCustomerFormOptions();
}
