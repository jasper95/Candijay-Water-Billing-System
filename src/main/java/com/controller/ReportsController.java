/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.InvoiceRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.domain.Invoice;
import com.domain.Schedule;
import com.forms.ChartForm;
import com.forms.ReportForm;
import com.service.InvoicingService;
import com.service.MeterReadingService;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
    
    @Autowired
    private MeterReadingService mrService;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private InvoiceRepository invoiceRepo;
    @Autowired
    private ScheduleRepository schedRepo;
    
    @RequestMapping(method=RequestMethod.GET)
    public String getReport(ModelMap model){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Map yearOptions = new LinkedHashMap();
        yearOptions.put("", "Select Year");
        for(int i = year; i >= 2014; i--)
            yearOptions.put(i, i);
        model.addAttribute("yearOptions", yearOptions);
        model.addAttribute("reportForm", new ReportForm());
        model.addAttribute("chartForm", new ChartForm());
        return "reports/report"; 
    }
    
    @RequestMapping(value="/validate-report-form", method=RequestMethod.POST)
    public @ResponseBody HashMap validateReportForm(@ModelAttribute("reportForm") @Valid  ReportForm reportForm, BindingResult result){
        HashMap response = new HashMap();
        if(schedRepo.findByMonthAndYear(reportForm.getMonth(), reportForm.getYear()) == null)
            result.rejectValue("","","No data avaialable for selected schedule");
        if(reportForm.getSummary().equals(new Integer(0)))
            if(reportForm.getBarangay().isEmpty())
                result.rejectValue("","","Please provide a barangay if it is not a summary");
        if(reportForm.getType().equals("bills"))
            if(reportForm.getBarangay().isEmpty())
                result.rejectValue("", "", "Please provide a barangay if it is for bills");
        if(!result.hasErrors()){
            response.put("status", "SUCCESS");
            response.put("result", reportForm);
        }else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
    
    @RequestMapping(value="/validate-chart-form", method=RequestMethod.POST)
    public @ResponseBody HashMap validateChartForm(@ModelAttribute("chartForm") @Valid  ChartForm chartForm, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            response.put("status", "SUCCESS");
            response.put("result", chartForm);
        }else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
    
    @RequestMapping(value="/print-report", method=RequestMethod.POST)
    public String getCollectiblesReport(ModelMap map, @RequestParam Map<String, String> params){
        String viewName;
        Schedule sched =  schedRepo.findByMonthAndYear(Integer.valueOf(params.get("month")), Integer.valueOf(params.get("year")));
        JRBeanCollectionDataSource datasource;
        if(params.get("type").equals("collectibles")){
            datasource = (JRBeanCollectionDataSource)invoicingService.getCollectiblesDataSource(params.get("barangay"), sched);
            viewName = "rpt_collectibles";
        }
        else if(params.get("type").equals("collection")) {
            datasource = (JRBeanCollectionDataSource)invoicingService.getCollectionDataSource(params.get("barangay"), sched);
            viewName = "rpt_collection";
        } else if(params.get("type").equals("bills")) {
            List<Invoice> invoices = invoiceRepo.findByScheduleAndAccount_Address_Brgy(sched, params.get("barangay"));
            datasource = (JRBeanCollectionDataSource)invoicingService.getDataSource(invoices);
            viewName = "rpt_bill";
        }else{
            map.put("type", "Invalid Parameter(s)");
            map.put("message", "Invalid report type");
            return "errors";
        }
        if(datasource.getData().size() > 0)
            map.put("datasource", datasource);
        else {
            map.put("type", "Invalid Parameter(s)");
            map.put("message", "No data for this type, month and year");
            return "errors";
        }
        if(params.get("summary").equals("0"))
            map.put("barangay", params.get("barangay"));
        else map.put("barangay", "Summary");
        map.put("format", "pdf");  
        return viewName;
    }
    
    @RequestMapping(value="/print-chart", method=RequestMethod.POST)
    public String getChart(ModelMap model, @RequestParam Map<String, String> params){
        JRBeanCollectionDataSource datasource;
        Integer year = year = Integer.valueOf(params.get("year"));
        Integer type = Integer.valueOf(params.get("type"));
        if(type.equals(new Integer(1))){
            model.put("CHART_TITLE", "Monthly Water Consumption");
            datasource = (JRBeanCollectionDataSource)invoicingService.getConsumptionChartDataSource(year);
        } else if(type.equals(new Integer(2))){
            model.put("CHART_TITLE", "Monthly Collectibles and Collection");
            datasource = (JRBeanCollectionDataSource)invoicingService.getCollectionCollectiblesChartDataSource(year);
        } else {
            model.put("type", "Invalid Parameter(s)");
            model.put("message", "Invalid chart type");
            return "errors";
        }
        if(datasource.getData().size() > 0){
            model.put("datasource", datasource);
        } else {
            model.put("type", "Invalid Parameter(s)");
            model.put("message", "No data for this type and year");
            return "errors";
        }
        model.put("format", "pdf");
        return "rpt_chart";
    }
}