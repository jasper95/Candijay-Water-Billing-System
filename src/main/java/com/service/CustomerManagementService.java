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
import java.util.List;
import com.domain.Address;
import com.domain.Device;
import com.domain.enums.AccountStatus;


/**
 *
 * @author Bert
 */
public interface CustomerManagementService {
    public Customer save(Customer customer);
    public Customer save(CustomerForm customerForm);
    public Device saveDevice(String accountNumber, Device device);
    public Account save(AccountForm accountForm);
    public void changeAccountStatus(Account account, AccountStatus status);
    public void activateDevice(Device device);
}
