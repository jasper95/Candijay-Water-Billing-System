/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.MeterReading;

/**
 *
 * @author Bert
 */
public interface InvoicingService extends ReportService{
    public void generateInvoiceMeterReading(MeterReading reading);
}
