/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Account;
import com.domain.Invoice;
import com.domain.Schedule;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author maebernales
 */
public interface ReportService {
    public JRDataSource getDataSource(List<Invoice> invoices);
    public JRDataSource getCollectiblesDataSource(String barangay, Schedule sched);
    public JRDataSource getCollectionDataSource(String barangay, Schedule sched);
    public JRDataSource getDisconnectionNoticeDataSource(List<Account> accounts);
    public JRDataSource getCollectionCollectiblesChartDataSource(Integer year);
    public JRDataSource getConsumptionChartDataSource(Integer year);
}
