/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;
import com.dao.springdatajpa.*;
import com.dao.util.EnglishNumberToWords;
import com.domain.*;
import com.domain.enums.InvoiceStatus;
import com.service.InvoicingService;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.util.*;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bert
 */
@Service("invoicingService")
public class InvoicingServiceImpl implements InvoicingService {

    private InvoiceRepository invoiceRepo;
    private MeterReadingRepository mrRepo;
    private PaymentRepository paymentRepo;
    private SettingsRepository settingsRepo;
    private AccountRepository accountRepo;
    private ScheduleRepository schedRepo;
    private ExpenseRepository expenseRepo;

    @Autowired
    public InvoicingServiceImpl(InvoiceRepository invoiceRepo, MeterReadingRepository mrRepo, PaymentRepository paymentRepo,
                                SettingsRepository settingsRepo, AccountRepository accountRepo, ScheduleRepository schedRepo, ExpenseRepository expenseRepo){

        this.invoiceRepo = invoiceRepo;
        this.mrRepo = mrRepo;
        this.paymentRepo = paymentRepo;
        this.settingsRepo = settingsRepo;
        this.accountRepo = accountRepo;
        this.schedRepo = schedRepo;
        this.expenseRepo = expenseRepo;
    }
    
    @Override
    public void generateInvoiceMeterReading(MeterReading reading) {
        Settings settings = settingsRepo.findAll().get(0);
        BigDecimal others = new BigDecimal(settings.getPes()), 
                    basic = new BigDecimal( reading.getConsumption()).multiply(new BigDecimal(settings.getBasic())),
                    total, systemLoss = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getSystemLoss())), 
                    depreciationFund = new BigDecimal(reading.getConsumption()).multiply(new BigDecimal(settings.getDepreciationFund())); 
        int month = reading.getSchedule().getMonth()+1;
        int year = reading.getSchedule().getYear();
        if(month > 12){
            month = 1;
            year++;
        }
        DateTime dueDate = new DateTime(year,
                                        month,
                                        reading.getAccount().getAddress().getAddressGroup().getDueDay(),
                                        0, 0);
        Invoice newInvoice = (reading.getInvoice() == null) ? new Invoice() : reading.getInvoice();
        newInvoice.setSchedule(reading.getSchedule());
        newInvoice.setDueDate(dueDate);
        newInvoice.setBasic(basic);
        newInvoice.setDepreciationFund(depreciationFund);
        newInvoice.setSystemLoss(systemLoss);
        total = basic.add(systemLoss.add(depreciationFund.add(others)));
        if(newInvoice.getId() == null){
            newInvoice.setAccount(reading.getAccount());
            newInvoice.setStatus(InvoiceStatus.UNPAID);
            newInvoice.setArrears(reading.getAccount().getAccountStandingBalance());
            newInvoice.setPenalty(reading.getAccount().getPenalty());
            newInvoice.setOthers(others);
            reading.setInvoice(newInvoice);
            newInvoice.setReading(reading);
            total = total.add(reading.getAccount().getAccountStandingBalance().add(reading.getAccount().getPenalty()));
        } else total = total.add(newInvoice.getArrears().add(newInvoice.getPenalty()));
        newInvoice.setNetCharge(total);
        invoiceRepo.save(newInvoice);
        reading.getAccount().setAccountStandingBalance(total);
        reading.getAccount().setStatusUpdated(false);
        accountRepo.save(reading.getAccount());
    }
    

    @Override
    public JRDataSource getDataSource(List<Invoice> invoices) {
        List<InvoiceReport> reports = new ArrayList<InvoiceReport>();
        for(Invoice invoice: invoices){
            MeterReading reading = invoice.getReading();
            InvoiceReport inv = new InvoiceReport();
            inv.setAccountId(invoice.getAccount().getId());
            inv.setAccountAddress(invoice.getAccount().getAddress().toString());
            inv.setCustomerName(invoice.getAccount().getCustomer().getFirstName()+" "+ invoice.getAccount().getCustomer().getLastname());
            inv.setScheduleMonth(invoice.getSchedule().getMonthSymbol());
            inv.setBillId(invoice.getId());
            inv.setReadingCurrent(reading.getReadingValue());
            inv.setReadingPrevious(reading.getReadingValue() - reading.getConsumption());
            inv.setReadingConsumption(reading.getConsumption());
            inv.setBillBasic(invoice.getBasic());
            inv.setBillDepFund(invoice.getDepreciationFund());
            inv.setBillPenalty(invoice.getPenalty());
            inv.setBillOthers(invoice.getOthers());
            inv.setBillSysLoss(invoice.getSystemLoss());
            inv.setBillArrears(invoice.getArrears());
            inv.setBillTotal(invoice.getNetCharge());
            inv.setDueDate(invoice.getDueDate().toDate());
            reports.add(inv);
        }
        return new JRBeanCollectionDataSource(reports);
    }
    
    @Transactional(readOnly=true)
    @Override
    public JRDataSource getCollectiblesDataSource(String barangay, Schedule sched) {
        List<Invoice> monthlyInvoiceByBarangay = null;
        if(!barangay.equalsIgnoreCase("summary"))
            monthlyInvoiceByBarangay = invoiceRepo.findByScheduleAndAccount_Address_Brgy(sched, barangay);
        else monthlyInvoiceByBarangay = invoiceRepo.findBySchedule(sched);
        List<CollectiblesReport> report = new ArrayList();
        report.add(new CollectiblesReport());
        for(Invoice invoice : monthlyInvoiceByBarangay){
            CollectiblesReport rpt = new CollectiblesReport();
            rpt.setAccountNo(invoice.getAccount().getNumber());
            rpt.setAmount(invoice.getNetCharge());
            rpt.setFirstName(invoice.getAccount().getCustomer().getFirstName());
            rpt.setLastName(invoice.getAccount().getCustomer().getLastname());
            rpt.setLocationCode(invoice.getAccount().getAddress().getLocationCode());
            rpt.setBillId(invoice.getId());
            rpt.setPenalty(invoice.getPenalty());
            rpt.setArrears(invoice.getArrears());
            rpt.setPes(invoice.getOthers());
            rpt.setSysLoss(invoice.getSystemLoss());
            rpt.setDepFund(invoice.getDepreciationFund());
            rpt.setBasic(invoice.getBasic());
            report.add(rpt);
        }
        return new JRBeanCollectionDataSource(report);
    }

    @Transactional(readOnly=true)
    @Override
    public JRDataSource getCollectionDataSource(String barangay, Schedule sched) {
        List<Payment> monthlyPaymentByBarangay = null;
        if(!barangay.equalsIgnoreCase("summary"))
            monthlyPaymentByBarangay = paymentRepo.findByInvoice_ScheduleAndAccount_Address_Brgy(sched, barangay);
        else monthlyPaymentByBarangay = paymentRepo.findByInvoice_Schedule(sched);
        List<CollectionReport> report = new ArrayList();
        report.add(new CollectionReport());
        for(Payment payment : monthlyPaymentByBarangay){
            CollectionReport rpt = new CollectionReport();
            rpt.setAccountNo(payment.getAccount().getNumber());
            rpt.setAmount(payment.getAmountPaid());
            rpt.setFirstName(payment.getAccount().getCustomer().getFirstName());
            rpt.setLastName(payment.getAccount().getCustomer().getLastname());
            rpt.setLocationCode(payment.getAccount().getAddress().getLocationCode());
            rpt.setOrNumber(payment.getReceiptNumber());
            rpt.setDue(payment.getInvoice().getNetCharge());
            rpt.setDiscount(payment.getDiscount());
            report.add(rpt);
        }
        return new JRBeanCollectionDataSource(report);
    }

    @Override
    public JRDataSource getDisconnectionNoticeDataSource(List<Account> accounts) {
        List<DisconnectionNotice> notices = new ArrayList();
        for(Account account : accounts){            
            DisconnectionNotice notice = new DisconnectionNotice();
            notice.setAccountId(account.getId());
            notice.setAccountNumber(null);
            long decimalPart = account.getAccountStandingBalance().longValue();
            String stringVal = account.getAccountStandingBalance().toPlainString();
            int index = stringVal.indexOf(".");
            long fractionPart = Long.valueOf(stringVal.substring(index+1, stringVal.length()));
            System.out.println(fractionPart);
            String balanceWords = EnglishNumberToWords.convert(decimalPart)+" pesos";
            if(fractionPart > 0){
                balanceWords += " and "+EnglishNumberToWords.convert(fractionPart) + " centavos";
            }
            notice.setBalanceWords(balanceWords);
            notice.setBalanceNumbers("(P "+account.getAccountStandingBalance()+" )");
            DateTime currDate = new DateTime();
            DateTime disconnectionDate = new DateTime(
                    currDate.getYear(), currDate.getMonthOfYear(), account.getAddress().getAddressGroup().getDueDay(), 0 ,0);
            disconnectionDate = disconnectionDate.plusDays(10);
            notice.setDisconnectionDate(disconnectionDate.toDate());
            String address = "Brgy "+account.getAddress().getBrgy()+" Zone "+account.getAddress().getLocationCode();
            String customerName = account.getCustomer().getLastname().toUpperCase() + ", " + account.getCustomer().getFirstName().toUpperCase() + " "
                    + account.getCustomer().getMiddleName().toUpperCase();
            notice.setAddress(address);
            notice.setCustomerName(customerName);
            notices.add(notice);
        }
        return new JRBeanCollectionDataSource(notices);
    }

    @Override
    public JRDataSource getCollectionCollectiblesChartDataSource(Integer year) {
        List<ChartData> list = new ArrayList();
        for(int i= 1; i <= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(new Integer(i), year);
            if(sched != null){
                String month = new DateFormatSymbols().getMonths()[sched.getMonth()-1];
                List<Invoice> invoices = invoiceRepo.findBySchedule(sched);
                List<Expense> expenses = expenseRepo.findBySchedule(sched);
                BigDecimal collectiblesTotal = BigDecimal.ZERO, collectionTotal = BigDecimal.ZERO, expensesTotal = BigDecimal.ZERO;
                for(Invoice invoice : invoices){
                    collectiblesTotal = collectiblesTotal.add(invoice.getNetCharge());
                    Payment payment = invoice.getPayment();
                    if(payment  != null){
                        collectionTotal = collectionTotal.add(payment.getAmountPaid());
                    }
                }
                for(Expense expense : expenses)
                    expensesTotal = expensesTotal.add(expense.getAmount());
                if(expensesTotal.compareTo(BigDecimal.ZERO) > 0)
                    list.add(new ChartData(expensesTotal, month, "Expenses"));
                if(collectiblesTotal.compareTo(BigDecimal.ZERO) > 0)
                    list.add(new ChartData(collectiblesTotal, month, "Collectibles"));
                if(collectionTotal.compareTo(BigDecimal.ZERO) > 0)
                    list.add(new ChartData(collectionTotal, month, "Collection"));
            }
        }
        return new JRBeanCollectionDataSource(list);
    }

    @Override
    public JRDataSource getConsumptionChartDataSource(Integer year){
        List<ChartData> list = new ArrayList();
        for(int i = 1; i <= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(new Integer(i), year);
            if(sched != null){
                String month = new DateFormatSymbols().getMonths()[sched.getMonth()-1];
                List<MeterReading> readings = mrRepo.findBySchedule(sched);
                BigDecimal readingTotal = BigDecimal.ZERO;
                for(MeterReading reading : readings){
                    readingTotal = readingTotal.add(new BigDecimal(reading.getConsumption()));
                }
                if(readingTotal.compareTo(BigDecimal.ZERO) > 0){
                    ChartData readingData = new ChartData(readingTotal, month, "Consumption");
                    list.add(readingData);
                }
            }
        }
        return new JRBeanCollectionDataSource(list);
    }

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
                BigDecimal collectiblesTotal = BigDecimal.ZERO, collectionTotal = BigDecimal.ZERO, wage1Total = BigDecimal.ZERO,
                        wage2Total = BigDecimal.ZERO, powerUsageTotal = BigDecimal.ZERO;
                for(Invoice invoice: invoiceRepo.findBySchedule(sched)){
                    collectiblesTotal = collectiblesTotal.add(invoice.getNetCharge());
                    Payment payment = invoice.getPayment();
                    if(payment != null){
                        collectionTotal = collectionTotal.add(payment.getAmountPaid());
                    }
                }
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
                    months.add(new DateFormatSymbols().getMonths()[sched.getMonth() - 1]);
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
        collectiblesDataset.put("backgroundColor", "rgba(0, 255, 0, 0.5)");
        collectiblesDataset.put("data", collectiblesData);
        collectiblesDataset.put("stack", 2);
        datasets.add(collectiblesDataset);
        HashMap wage1Dataset = new HashMap();
        wage1Dataset.put("label", "Wage(1-15)");
        wage1Dataset.put("backgroundColor", "rgba(255, 165, 0, 0.5)");
        wage1Dataset.put("data", wage1Data);
        wage1Dataset.put("stack", 3);
        datasets.add(wage1Dataset);
        HashMap wage2Dataset = new HashMap();
        wage2Dataset.put("label", "Wage(16-30)");
        wage2Dataset.put("backgroundColor", "rgba(255, 255, 0, 0.5)");
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

    @Override
    public HashMap getConsumptionDataSource(Integer year) {
        HashMap data = new HashMap();
        ArrayList<String> months = new ArrayList();
        ArrayList<BigInteger> readings = new ArrayList();
        ArrayList<HashMap> datasets = new ArrayList();
        for(int i=1; i<= 12; i++){
            Schedule sched = schedRepo.findByMonthAndYear(i, year);
            if(sched != null){
                BigInteger readingTotal = BigInteger.ZERO;
                for(MeterReading reading: mrRepo.findBySchedule(sched)){
                    readingTotal = readingTotal.add(new BigInteger(reading.getConsumption().toString()));
                }
                if(readingTotal.compareTo(BigInteger.ZERO) > 0){
                    months.add(new DateFormatSymbols().getMonths()[sched.getMonth()-1]);
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
