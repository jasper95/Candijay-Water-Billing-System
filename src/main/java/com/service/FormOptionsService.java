/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import java.util.Collections;
import java.util.HashMap;

public interface FormOptionsService{
    public HashMap<String,Collections> getCustomerFormOptions();
    public HashMap<String,Collections> getMeterReadingFormOptions();
    public HashMap<String, Collections> getReportFormOptions();
    public HashMap<String, Collections> getExpenseFormOptions();
}