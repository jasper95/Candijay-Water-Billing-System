/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import java.util.Collections;
import java.util.HashMap;

public interface FormOptionsService{
    HashMap<String,Collections> getCustomerFormOptions();
    HashMap<String,Collections> getMeterReadingFormOptions();
    HashMap<String, Collections> getReportFormOptions();
    HashMap<String, Collections> getExpenseFormOptions();
}