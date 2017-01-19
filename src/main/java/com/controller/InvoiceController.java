/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.InvoiceRepository;
import com.domain.Invoice;
import com.domain.enums.InvoiceStatus;
import com.forms.BillDiscountForm;
import com.forms.Checkboxes;
import com.service.DataTableService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;

import com.service.InvoicingService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author maebernales
 */

@Controller
@RequestMapping("/admin/bills")
public class InvoiceController {
    
    private InvoiceRepository invoiceRepo;
    private DataTableService dataTableService;
    private InvoicingService invoiceService;

    @Autowired
    public InvoiceController(DataTableService dataTableService, InvoiceRepository invoiceRepo,
                             InvoicingService invoiceService) {
        this.dataTableService = dataTableService;
        this.invoiceRepo = invoiceRepo;
        this.invoiceService = invoiceService;
    }
    

    @RequestMapping(method=RequestMethod.GET)
    public String allBills(ModelMap model){
        model.addAttribute("checkboxes", new Checkboxes());
        model.addAttribute("billDiscountForm", new BillDiscountForm());
        return "bills/billList";
    }

    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public @ResponseBody HashMap getInvoice(@PathVariable("id") Long id){
        Invoice result = invoiceRepo.findById(id);
        HashMap response = new HashMap();
        String status = (result != null) ? "SUCCESS" : "FAILURE";
        response.put("status", status);
        response.put("result", result.getReading());
        return response;
    }


    @RequestMapping(value="/print-check", method=RequestMethod.POST)
    public @ResponseBody
    HashMap checkInvoiceIds(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        HashMap response = new HashMap();
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("errors", result.getAllErrors());
        } else {
            response.put("status","SUCCESS");
            response.put("result",checkboxes);
        }
        return response;
    }

    @RequestMapping(value="/print", method=RequestMethod.POST)
    public String getBill(ModelMap modelMap, @ModelAttribute("checkboxes") Checkboxes checkboxes) {
        List<Invoice> invoices = new ArrayList();
        for(Long id : checkboxes.getCheckboxValues())
            invoices.add(invoiceRepo.findById(id));
        modelMap.put("datasource", new JRBeanCollectionDataSource(invoices));
        modelMap.put("format", "pdf");
        return "rpt_bill";
    }

    @RequestMapping(value="/preview", method=RequestMethod.POST)
    public String previewBill(ModelMap model, @RequestParam("id") Long id){
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoiceRepo.findById(id));
        model.put("datasource", new JRBeanCollectionDataSource(invoices));
        model.put("format", "pdf");
        return "rpt_bill";
    }

    @RequestMapping(value="/edit-discount/{id}")
    public @ResponseBody HashMap findBill(@PathVariable("id") Long id){
        Invoice invoice = invoiceRepo.findById(id),
                latestInvoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(invoice.getAccount());
        HashMap response = new HashMap();
        if(invoice != null && invoice.equals(latestInvoice) && invoice.getStatus().equals(InvoiceStatus.UNPAID) || invoice.getStatus().equals(InvoiceStatus.DEBT)) {
            response.put("invoice", invoice);
            response.put("status", "SUCCESS");
        } else response.put("status", "FAILURE");
        return response;
    }

    @RequestMapping(value = "/update-discount", method = RequestMethod.POST)
    public @ResponseBody HashMap updateDiscount(@ModelAttribute("billDiscountForm") @Valid BillDiscountForm form, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            response.put("invoice", invoiceService.updateDiscount(form));
            response.put("status", "SUCCESS");
        } if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
}