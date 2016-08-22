/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.InvoiceRepository;
import com.domain.Invoice;
import com.forms.Checkboxes;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.DataTableService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author maebernales
 */

@Controller
@RequestMapping("/admin/bills")
public class InvoiceController {
    
    private InvoiceRepository invoiceRepo;
    private DataTableService dataTableService;
    
    @Autowired
    public InvoiceController(DataTableService dataTableService, InvoiceRepository invoiceRepo) {
        this.dataTableService = dataTableService;
        this.invoiceRepo = invoiceRepo;
    }
    
    
    @RequestMapping(method=RequestMethod.GET)
    public String allBills(ModelMap model){
        model.addAttribute("checkboxes", new Checkboxes());
        return "bills/billList";
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
            invoices.add(invoiceRepo.findOne(id));
        modelMap.put("datasource", new JRBeanCollectionDataSource(invoices));
        modelMap.put("format", "pdf");
        return "rpt_bill";
    }
    
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<Invoice> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
       DataSet<Invoice> dataSet = dataTableService.findWithDataTableCriterias(criterias, Invoice.class);
       return DatatablesResponse.build(dataSet, criterias);
    }
}