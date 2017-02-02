/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.AddressRepository;
import com.dao.springdatajpa.InvoiceRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.dao.util.EnglishNumberToWords;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.forms.AccountabilityReportForm;
import com.forms.ChartForm;
import com.forms.ReportForm;
import com.service.FormOptionsService;

import java.util.*;
import javax.validation.Valid;

import com.service.MeterReadingService;
import com.service.ReportService;
import com.service.SettingsService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Bert
 */
@RequestMapping("/admin/reports")
@Controller
public class ReportsController {

    private ReportService reportService;
    private ScheduleRepository schedRepo;
    private FormOptionsService formOptionsService;
    private AddressRepository addressRepo;
    private AccountRepository accountRepo;
    private InvoiceRepository invoiceRepo;
    private SettingsService settingsService;

    @Autowired
    public ReportsController(ReportService reportService, ScheduleRepository schedRepo, FormOptionsService formOptionsService,
                             AddressRepository addressRepo, AccountRepository accountRepo, InvoiceRepository invoiceRepo,
                             SettingsService settingsService){
        this.reportService = reportService;
        this.schedRepo = schedRepo;
        this.formOptionsService = formOptionsService;
        this.addressRepo = addressRepo;
        this.accountRepo = accountRepo;
        this.invoiceRepo = invoiceRepo;
        this.settingsService = settingsService;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String getReport(ModelMap model){
        HashMap options = formOptionsService.getReportFormOptions();
        model.addAttribute("reportForm", new ReportForm());
        model.addAttribute("chartForm", new ChartForm());
        model.addAttribute("addressForm", new AccountabilityReportForm());
        model.addAttribute("yearOptions", options.get("year"));
        model.addAttribute("brgyOptions", options.get("brgy"));
        model.addAttribute("monthOptions", options.get("month"));
        model.addAttribute("typeOptionsReport", options.get("typeReport"));
        model.addAttribute("typeOptionsChart", options.get("typeChart"));
        model.addAttribute("typeOptionsAcctblty", options.get("acctblty"));
        model.addAttribute("zoneOptions", options.get("zone"));
        return "reports/report";
    }

    public HashMap validationResponse(BindingResult result, Object form){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            response.put("status", "SUCCESS");
            response.put("result", form);
        } else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }

    @RequestMapping(value="/validate-report-form", method=RequestMethod.POST)
    public @ResponseBody HashMap validateReportForm(@ModelAttribute("reportForm") @Valid  ReportForm reportForm, BindingResult result){
        if(!result.hasErrors()) {
            if (schedRepo.findByMonthAndYear(reportForm.getMonth(), reportForm.getYear()) == null) {
                result.reject("global", "No data avaialable for selected schedule");
                result.rejectValue("month", "", "");
                result.rejectValue("year", "", "");
            }
            if (reportForm.getSummary().equals(0) && reportForm.getBarangay().isEmpty())
                result.rejectValue("barangay", "", "Please provide a barangay if it is not a summary");
        }
        return validationResponse(result, reportForm);
    }

    @RequestMapping(value="/validate-accountability-form", method=RequestMethod.POST)
    public @ResponseBody HashMap validateAccountabilityForm(@ModelAttribute("addressForm") @Valid  AccountabilityReportForm form, BindingResult result){
        List<Address> addresses = new ArrayList();
        boolean isPrintBrgy = form.getPrintBrgy().equals(1);
        if(!result.hasErrors()){
            if(isPrintBrgy){
                if(form.getBarangay().isEmpty())
                    result.rejectValue("barangay", "", "This field is required");
                else
                    addresses.add(addressRepo.findByBrgy(form.getBarangay()));
            } else {
                if(form.getZone() == null)
                    result.rejectValue("zone", "", "This field is required");
                else
                    addresses = addressRepo.findByLocationCode(form.getZone());

            }
        }
        if(!result.hasErrors()){
            Integer type = Integer.valueOf(form.getType());
            boolean billsFlag = type.equals(1);
            Long statusUpdatedCount = accountRepo.countByAddressInAndStatusUpdated(addresses, true), accountsCount = accountRepo.countByAddressIn(addresses);
            System.out.println(String.format("%s %s", statusUpdatedCount, accountsCount));
            if(!billsFlag && !statusUpdatedCount.equals(accountsCount))
                result.reject("global", "Payments not finalized for this address");
            if(result.hasErrors())
                if(isPrintBrgy)
                    result.rejectValue("barangay", "", "");
                else
                    result.rejectValue("zone", "", "");
        }
        return validationResponse(result, form);
    }

    @RequestMapping(value="/validate-chart-form", method=RequestMethod.POST)
    public @ResponseBody HashMap validateChartForm(@ModelAttribute("chartForm") @Valid  ChartForm chartForm, BindingResult result){
        return validationResponse(result, chartForm);
    }

    @RequestMapping(value="/print-accountability", method=RequestMethod.POST)
    public String getAccountabilityReport(ModelMap model, @RequestParam Map<String,String> params){
        boolean isPrintBrgy = Integer.valueOf(params.get("printBrgy")) == 1;
        Integer type = Integer.valueOf(params.get("type"));
        List<Address> addresses = new ArrayList();
        if(isPrintBrgy)
            addresses.add(addressRepo.findByBrgy(params.get("barangay")));
        else addresses = addressRepo.findByLocationCode(Integer.valueOf(params.get("zone")));
        JRBeanCollectionDataSource dataSource;
        String viewName;
        if(type == 1) {
            List<Account> accounts = accountRepo.findByAddressInAndStatusIn(addresses, Arrays.asList(AccountStatus.ACTIVE, AccountStatus.WARNING));
            List<Invoice> invoices = new ArrayList();
            for (Account account : accounts)
                invoices.add(invoiceRepo.findTopByAccount_IdOrderBySchedule_YearDescSchedule_MonthDesc(account.getId()));
            dataSource = new JRBeanCollectionDataSource(invoices);
            viewName = "rpt_bill";
        } else {
            List<Account> accounts = accountRepo.findByAddressInAndStatusUpdatedAndStatusIn(addresses, true, Arrays.asList(AccountStatus.WARNING));
            dataSource = new JRBeanCollectionDataSource(accounts);
            model.put("DEBTS_ALLOWED", EnglishNumberToWords.covertIntNumberToBisaya(settingsService.getCurrentSettings().getDebtsAllowed()));
            viewName = "rpt_disconnection_notice";
        }
        if(dataSource.getData().size() > 0){
            model.put("datasource", dataSource);
        } else {
            model.put("type", "No data available for this report");
            if(type == 1)
                model.put("message", "Not finished encoding meter reading for this address.");
            else
                model.put("message", "No account in this address that has WARNING status.");

            return "errors";
        }
        model.put("format", "pdf");
        return viewName;
    }

    @RequestMapping(value="/print-report", method=RequestMethod.POST)
    public String getCollectiblesReport(ModelMap map, @RequestParam Map<String, String> params){
        String viewName;
        Integer month = Integer.valueOf(params.get("month")), year = Integer.valueOf(params.get("year")), summary = Integer.valueOf(params.get("summary"));
        Schedule sched =  schedRepo.findByMonthAndYear(month, year);
        JRBeanCollectionDataSource datasource;
        map.put("month", new LocalDate(2010, month, 1).toString("MMM"));
        map.put("year", year.toString());
        Integer type = Integer.valueOf(params.get("type"));
        String barangay = (summary.equals(1)) ? "Summary" : params.get("barangay");
        if(type.equals(1)){
            datasource = (JRBeanCollectionDataSource)reportService.getCollectiblesDataSource(barangay, sched);
            viewName = "rpt_table2";
        }
        else if(type.equals(2)) {
            datasource = (JRBeanCollectionDataSource)reportService.getCollectionDataSource(barangay, sched);
            viewName = "rpt_payment_history";
            map.put("IS_HISTORY", false);
        } else {
            map.put("type", "Invalid Parameter(s)");
            map.put("message", "Invalid report type");
            return "errors";
        }
        if(datasource.getData().size() > 1)
            map.put("datasource", datasource);
        else {
            map.put("type", "No data available for this report");
            map.put("message", "No collection/collectibles data for this type, month and year");
            return "errors";
        }
        map.put("barangay", barangay);
        map.put("format", "pdf");  
        return viewName;
    }
    
    @RequestMapping(value="/print-chart", method=RequestMethod.POST)
    public String getChart(ModelMap model, @RequestParam Map<String, String> params){
        JRBeanCollectionDataSource datasource;
        Integer year = Integer.valueOf(params.get("year"));
        Integer type = Integer.valueOf(params.get("type"));
        String viewName;
        if(type.equals(1)){
            model.put("CHART_TITLE", "Monthly Water Consumption for "+year);
            model.put("VALUE_LABEL", "Cubic Meters");
            viewName = "rpt_chart";
            datasource = (JRBeanCollectionDataSource)reportService.getConsumptionChartDataSource(year);
        } else if(type.equals(2)){
            model.put("CHART_TITLE", "Monthly Collectibles, Collection and Expenses for "+year);
            model.put("VALUE_LABEL", "Amount");
            viewName = "rpt_chart2";
            datasource = (JRBeanCollectionDataSource)reportService.getCollectionCollectiblesChartDataSource(year);
        } else {
            model.put("type", "Invalid Parameter(s)");
            model.put("message", "Invalid chart type");
            return "errors";
        }
        if(datasource.getData().size() > 0){
            model.put("datasource", datasource);
        } else {
            model.put("type", "No data available");
            model.put("message", "No chart data for this type and year");
            return "errors";
        }
        model.put("format", "pdf");
        model.put("CATEGORY_LABEL", "Months");
        return viewName;
    }
}