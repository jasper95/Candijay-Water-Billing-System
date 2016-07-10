/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import com.domain.Customer;
import com.domain.Occupation;
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
    
}
