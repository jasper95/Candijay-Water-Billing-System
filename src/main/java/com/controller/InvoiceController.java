/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.InvoiceRepository;
import com.domain.Invoice;
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

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
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
}