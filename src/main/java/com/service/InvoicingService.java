/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Invoice;
import com.domain.MeterReading;
import com.forms.BillDiscountForm;

/**
 *
 * @author Bert
 */
public interface InvoicingService{
    void generateInvoiceMeterReading(MeterReading reading);
    Invoice updateDiscount(BillDiscountForm form);
}
