/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import com.domain.*;

import javax.validation.Valid;

/**
 *
 * @author Bert
 */
public class CustomerForm {
    @Valid
    private Customer customer;
    @Valid
    private Occupation occupation;
    @Valid
    private Account account;
    @Valid
    private Device device;
    @Valid
    private Address address;
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }

    public Device getDevice() { return device; }

    public void setDevice(Device device) { this.device = device; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }
}
