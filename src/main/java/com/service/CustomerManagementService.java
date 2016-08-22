/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.*;
import com.forms.AccountForm;
import com.forms.CustomerForm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.domain.enums.AccountStatus;


/**
 *
 * @author Bert
 */
public interface CustomerManagementService {
    Customer updateCustomer(CustomerForm customerForm);
    Customer createCustomer(CustomerForm customerForm);
    Device saveNewDevice(String accountNumber, Device device);
    Device updateDevice(Long id, Device device);
    Account createAccount(AccountForm accountForm);
    Account updateAccount(AccountForm accountForm);
    void changeAccountStatus(Account account, AccountStatus status);
    void activateDevice(Device device);
}
