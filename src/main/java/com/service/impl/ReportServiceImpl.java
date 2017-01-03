package com.service.impl;

import com.charts.ChartData;
import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.InvoiceStatus;
import com.service.ReportService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jasper on 8/18/16.
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    private InvoiceRepository invoiceRepo;
    private MeterReadingRepository mrRepo;
    private PaymentRepository paymentRepo;
    private ScheduleRepository schedRepo;
    private ExpenseRepository expenseRepo;
    private AddressRepository addressRepo;

    @Autowired
    public ReportServiceImpl(InvoiceRepository invoiceRepo, MeterReadingRepository mrRepo, PaymentRepository paymentRepo,
                             ScheduleRepository schedRepo, ExpenseRepository expenseRepo, AddressRepository addressRepo){

        this.invoiceRepo = invoiceRepo;
        this.mrRepo = mrRepo;
        this.paymentRepo = paymentRepo;
        this.schedRepo = schedRepo;
        this.expenseRepo = expenseRepo;
        this.addressRepo = addressRepo;
    }

    @Transactional(readOnly=true)
    @Override
    public JRDataSource getCollectiblesDataSource(String barangay, Schedule sched) {
        List<Invoice> monthlyInvoiceByBarangay = (!barangay.equalsIgnoreCase("summary")) ?
                invoiceRepo.findByScheduleAndAccount_AddressAndStatusNotOrderByAccount_Customer_LastnameAscAccount_Customer_FirstNameAsc(sched, addressRepo.findByBrgy(barangay), InvoiceStatus.FULLYPAID) :
                invoiceRepo.findByScheduleAndStatusNotOrderByAccount_Customer_LastnameAscAccount_Customer_FirstNameAsc(sched, InvoiceStatus.FULLYPAID);
        monthlyInvoiceByBarangay.add(0, new Invoice());
        return new JRBeanCollectionDataSource(monthlyInvoiceByBarangay);
    }

    @Transactional(readOnly=true)
    @Override
    public JRDataSource getCollectionDataSource(String barangay, Schedule sched) {
        List<Payment> monthlyPaymentByBarangay = (!barangay.equalsIgnoreCase("summary")) ?
                paymentRepo.findByScheduleAndAccount_AddressAndInvoice_StatusNot(sched.getId(), addressRepo.findByBrgy(barangay).getId(), InvoiceStatus.DEBT) :
                paymentRepo.findByScheduleAndInvoice_StatusNot(sched.getId(), InvoiceStatus.DEBT);
        monthlyPaymentByBarangay.add(0, new Payment());
        return new JRBeanCollectionDataSource(monthlyPaymentByBarangay);
    }

    @Transactional(readOnly = true)
    @Override
    public JRDataSource getCollectionCollectiblesChartDataSource(Integer year) {
        List<ChartData> list = new ArrayList();
        for(int i= 1; i <= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(i, year);
            if(sched != null){
                String month = new DateFormatSymbols().getShortMonths()[sched.getMonth()-1];
                List<Expense> expenses = expenseRepo.findBySchedule(sched);
                BigDecimal queryCollectibles = invoiceRepo.findTotalCollectiblesBySchedule(sched.getId()), queryCollection = paymentRepo.findTotalCollectionBySchedule(sched.getId());
                BigDecimal collectiblesTotal = (queryCollectibles != null) ? queryCollectibles: BigDecimal.ZERO,
                        collectionTotal = (queryCollection != null) ? queryCollection : BigDecimal.ZERO,
                        wage1Total = BigDecimal.ZERO, wage2Total = BigDecimal.ZERO, powerUsageTotal = BigDecimal.ZERO;
                for(Expense expense : expenses){
                    switch(expense.getType()){
                        case 1:
                            wage1Total = wage1Total.add(expense.getAmount());
                            break;
                        case 2:
                            wage2Total = wage2Total.add(expense.getAmount());
                            break;
                        case 3:
                            powerUsageTotal = powerUsageTotal.add(expense.getAmount());
                            break;
                    }
                }
                list.add(new ChartData(collectionTotal, month, "Collection"));
                list.add(new ChartData(collectiblesTotal, month, "Collectibles"));
                list.add(new ChartData(wage1Total, month, "Wage(1-15)"));
                list.add(new ChartData(wage2Total, month, "Wage(16-30)"));
                list.add(new ChartData(powerUsageTotal, month, "Power Usage"));

            }
        }
        return new JRBeanCollectionDataSource(list);
    }

    @Transactional(readOnly = true)
    @Override
    public JRDataSource getConsumptionChartDataSource(Integer year){
        List<ChartData> list = new ArrayList();
        for(int i = 1; i <= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(new Integer(i), year);
            if(sched != null){
                String month = new DateFormatSymbols().getShortMonths()[sched.getMonth()-1];
                BigInteger queryReading = mrRepo.findTotalConsumptionBySchedule(sched.getId());
                BigInteger readingTotal = (queryReading != null) ? queryReading : BigInteger.ZERO;
                if(readingTotal.compareTo(BigInteger.ZERO) > 0){
                    ChartData readingData = new ChartData(new BigDecimal(readingTotal), month, "Consumption");
                    list.add(readingData);
                }
            }
        }
        return new JRBeanCollectionDataSource(list);
    }

    @Transactional(readOnly = true)
    @Override
    public HashMap getCollectionCollectiblesExpenseDataSource(Integer year) {
        HashMap data = new HashMap();
        ArrayList<String> months = new ArrayList<String>();
        ArrayList<BigDecimal> collectiblesData = new ArrayList(),
                collectionData = new ArrayList(), wage1Data = new ArrayList(), wage2Data = new ArrayList(), powerUsageData = new ArrayList();
        ArrayList<HashMap> datasets = new ArrayList();
        for(int i=1; i<=12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(i, year);
            if(sched != null){
                BigDecimal queryCollectibles = invoiceRepo.findTotalCollectiblesBySchedule(sched.getId()), queryCollection = paymentRepo.findTotalCollectionBySchedule(sched.getId());
                BigDecimal collectiblesTotal = (queryCollectibles != null) ? queryCollectibles: BigDecimal.ZERO,
                        collectionTotal = (queryCollection != null) ? queryCollection : BigDecimal.ZERO,
                        wage1Total = BigDecimal.ZERO, wage2Total = BigDecimal.ZERO, powerUsageTotal = BigDecimal.ZERO;
                for(Expense expense : expenseRepo.findBySchedule(sched)){
                    switch(expense.getType()){
                        case 1:
                            wage1Total = wage1Total.add(expense.getAmount());
                            break;
                        case 2:
                            wage2Total = wage2Total.add(expense.getAmount());
                            break;
                        case 3:
                            powerUsageTotal = powerUsageTotal.add(expense.getAmount());
                            break;
                    }
                }
                if(collectiblesTotal.compareTo(BigDecimal.ZERO) > 0 || collectionTotal.compareTo(BigDecimal.ZERO) > 0 ||
                        wage1Total.compareTo(BigDecimal.ZERO) > 0 || wage2Total.compareTo(BigDecimal.ZERO) > 0 || powerUsageTotal.compareTo(BigDecimal.ZERO) > 0) {
                    months.add(new DateFormatSymbols().getShortMonths()[sched.getMonth() - 1]);
                    collectiblesData.add(collectiblesTotal);
                    collectionData.add(collectionTotal);
                    wage1Data.add(wage1Total);
                    wage2Data.add(wage2Total);
                    powerUsageData.add(powerUsageTotal);
                }
            }
        }
        HashMap collectionDataset = new HashMap();
        collectionDataset.put("label", "Collection");
        collectionDataset.put("backgroundColor", "rgba(255, 0, 0, 0.5)");
        collectionDataset.put("data", collectionData);
        collectionDataset.put("stack", 1);
        datasets.add(collectionDataset);
        HashMap collectiblesDataset = new HashMap();
        collectiblesDataset.put("label", "Collectibles");
        collectiblesDataset.put("backgroundColor", "rgba(0, 0, 255, 0.5)");
        collectiblesDataset.put("data", collectiblesData);
        collectiblesDataset.put("stack", 2);
        datasets.add(collectiblesDataset);
        HashMap wage1Dataset = new HashMap();
        wage1Dataset.put("label", "Wage(1-15)");
        wage1Dataset.put("backgroundColor", "rgba(0, 255, 0, 0.5)");
        wage1Dataset.put("data", wage1Data);
        wage1Dataset.put("stack", 3);
        datasets.add(wage1Dataset);
        HashMap wage2Dataset = new HashMap();
        wage2Dataset.put("label", "Wage(16-30)");
        wage2Dataset.put("backgroundColor", "rgba(255, 165, 0, 0.5)");
        wage2Dataset.put("data", wage2Data);
        wage2Dataset.put("stack", 3);
        datasets.add(wage2Dataset);
        HashMap powerUsageDataset = new HashMap();
        powerUsageDataset.put("label", "Power Usage");
        powerUsageDataset.put("backgroundColor", "rgba(128, 0, 128, 0.5)");
        powerUsageDataset.put("data", powerUsageData);
        powerUsageDataset.put("stack", 3);
        datasets.add(powerUsageDataset);
        data.put("labels", months);
        data.put("datasets", datasets);
        return data;
    }

    @Transactional(readOnly = true)
    @Override
    public HashMap getConsumptionDataSource(Integer year) {
        HashMap data = new HashMap();
        ArrayList<String> months = new ArrayList();
        ArrayList<BigInteger> readings = new ArrayList();
        ArrayList<HashMap> datasets = new ArrayList();
        for(int i=1; i<= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(i, year);
            if(sched != null){
                BigInteger queryReading = mrRepo.findTotalConsumptionBySchedule(sched.getId());
                BigInteger readingTotal = (queryReading != null) ? queryReading: BigInteger.ZERO;
                if(readingTotal.compareTo(BigInteger.ZERO) > 0){
                    months.add(new DateFormatSymbols().getShortMonths()[sched.getMonth()-1]);
                    readings.add(readingTotal);
                }
            }
        }
        HashMap consumptionDataset = new HashMap();
        consumptionDataset.put("label", "Water Consumption");
        consumptionDataset.put("backgroundColor", "rgba(255, 0, 0, 0.5)");
        consumptionDataset.put("data", readings);
        consumptionDataset.put("stack", 1);
        datasets.add(consumptionDataset);
        data.put("labels", months);
        data.put("datasets", datasets);
        return data;
    }
}
