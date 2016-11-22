/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.Invoice;
import com.domain.Schedule;

import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * interface created for services used in reports module.
 * @author jasper
 */
public interface ReportService {
    JRDataSource getCollectiblesDataSource(String barangay, Schedule sched);
    JRDataSource getCollectionDataSource(String barangay, Schedule sched);
    JRDataSource getCollectionCollectiblesChartDataSource(Integer year);
    JRDataSource getConsumptionChartDataSource(Integer year);
    HashMap getCollectionCollectiblesExpenseDataSource(Integer year);
    HashMap getConsumptionDataSource(Integer year);
}
