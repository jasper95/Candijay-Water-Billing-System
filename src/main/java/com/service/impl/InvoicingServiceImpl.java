/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;
import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.InvoiceRepository;
import com.dao.springdatajpa.MeterReadingRepository;
import com.dao.springdatajpa.PaymentRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.dao.springdatajpa.SettingsRepository;
import com.dao.util.EnglishNumberToWords;
import com.domain.Account;
import com.domain.CollectiblesReport;
import com.domain.ChartData;
import com.domain.CollectionReport;
import com.domain.DisconnectionNotice;
import com.domain.Invoice;
import com.domain.InvoiceReport;
import com.domain.MeterReading;
import com.domain.Payment;
import com.domain.Schedule;
import com.domain.Settings;
import com.domain.enums.InvoiceStatus;
import com.service.InvoicingService;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

    @Autowired
    private InvoiceRepository invoiceRepo;
    @Autowired
    private MeterReadingRepository mrRepo;
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private SettingsRepository settingsRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private ScheduleRepository schedRepo;
    
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
        newInvoice.setAccount(reading.getAccount());
        newInvoice.setStatus(InvoiceStatus.UNPAID);
        newInvoice.setDueDate(dueDate);
        newInvoice.setBasic(basic);
        newInvoice.setDepreciationFund(depreciationFund);
        newInvoice.setSystemLoss(systemLoss);
        newInvoice.setArrears(reading.getAccount().getAccountStandingBalance());
        newInvoice.setPenalty(reading.getAccount().getPenalty());
        newInvoice.setOthers(others);
        total = basic.add(systemLoss.add(depreciationFund.add(others)));
        total = total.add(reading.getAccount().getAccountStandingBalance());
        total = total.add(reading.getAccount().getPenalty());
        newInvoice.setNetCharge(total);
        reading.setInvoice(newInvoice);
        newInvoice.setReading(reading);
        invoiceRepo.save(newInvoice);
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
        if(!barangay.isEmpty())
            monthlyInvoiceByBarangay = invoiceRepo.findByScheduleAndAccount_Address_Brgy(sched, barangay);
        else monthlyInvoiceByBarangay = invoiceRepo.findBySchedule(sched);
        List<CollectiblesReport> report = new ArrayList();
        for(Invoice invoice : monthlyInvoiceByBarangay){
            CollectiblesReport rpt = new CollectiblesReport();
            rpt.setAccountNo(invoice.getAccount().getId());
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
        if(!barangay.isEmpty())
            monthlyPaymentByBarangay = paymentRepo.findByInvoice_ScheduleAndAccount_Address_Brgy(sched, barangay);
        else monthlyPaymentByBarangay = paymentRepo.findByInvoice_Schedule(sched);
        List<CollectionReport> report = new ArrayList();
        for(Payment payment : monthlyPaymentByBarangay){
            CollectionReport rpt = new CollectionReport();
            rpt.setAccountNo(payment.getAccount().getId());
            rpt.setAmount(payment.getAmountPaid());
            rpt.setFirstName(payment.getAccount().getCustomer().getFirstName());
            rpt.setLastName(payment.getAccount().getCustomer().getLastname());
            rpt.setLocationCode(payment.getAccount().getAddress().getLocationCode());
            rpt.setOrNumber(payment.getId());
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
                BigDecimal collectiblesTotal = BigDecimal.ZERO, collectionTotal = BigDecimal.ZERO;
                for(Invoice invoice : invoices){
                    collectiblesTotal = collectiblesTotal.add(invoice.getNetCharge());
                    Payment payment = invoice.getPayment();
                    if(payment  != null){
                        collectionTotal = collectionTotal.add(payment.getAmountPaid());
                    }
                }
                if(collectiblesTotal.compareTo(BigDecimal.ZERO) > 0){
                    ChartData monthCollectiblesData = new ChartData(collectiblesTotal, month, "Collectibles");
                    list.add(monthCollectiblesData);
                }
                if(collectionTotal.compareTo(BigDecimal.ZERO) > 0){
                    ChartData monthCollectionData = new ChartData(collectionTotal, month, "Collection");
                    list.add(monthCollectionData); 
                }
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

}
