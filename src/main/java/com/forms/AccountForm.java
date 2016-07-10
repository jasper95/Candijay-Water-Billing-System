/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import com.domain.Account;
import com.domain.Address;
import com.domain.Device;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Bert
 */
public class AccountForm {
    @Valid
    private Account account;
    @Valid
    private Device device;
    @NotNull
    private Long customerId;
    @Valid
    private Address address;
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
}
