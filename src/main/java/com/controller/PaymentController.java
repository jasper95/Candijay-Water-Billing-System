/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.AddressRepository;
import com.dao.springdatajpa.InvoiceRepository;
import com.domain.*;
import com.forms.Checkboxes;
import com.forms.PaymentForm;
import com.forms.SearchForm;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.DataTableService;
import com.service.FormOptionsService;
import com.service.PaymentService;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author 201244055
 */
@RequestMapping("/admin/payments")
@Controller
public class PaymentController {

    private DataTableService dataTableService;
    private PaymentService paymentService;
    private AccountRepository accountRepo;
    private InvoiceRepository invoiceRepo;
    private FormOptionsService formOptionsService;
    private AddressRepository addressRepo;
    
    @Autowired
    public PaymentController(DataTableService dataTableService, PaymentService paymentService, 
                             AccountRepository accountRepo, InvoiceRepository invoiceRepo, FormOptionsService formOptionsService,
                             AddressRepository addressRepo) {
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.accountRepo = accountRepo;
        this.invoiceRepo = invoiceRepo;
        this.formOptionsService = formOptionsService;
        this.addressRepo = addressRepo;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String allPayments(ModelMap model){
        model.addAttribute("paymentForm", new PaymentForm());
        model.addAttribute("checkboxes", new Checkboxes());
        HashMap finalizePaymentFormOptions = formOptionsService.getCustomerFormOptions();
        model.addAttribute("brgyOptions", finalizePaymentFormOptions.get("brgy"));
        model.addAttribute("zoneOptions", finalizePaymentFormOptions.get("zone"));
        model.addAttribute("addressForm", new Address());
        return "payments/paymentList";
    }
    
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<Payment> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
       DataSet<Payment> dataSet = dataTableService.findWithDataTableCriterias(criterias, Payment.class);
       return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/modified/datatable-search")
    public @ResponseBody
    DatatablesResponse<ModifiedPayment> findAllModifiedPayments(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<ModifiedPayment> dataSet = dataTableService.findWithDataTableCriterias(criterias, ModifiedPayment.class);
        return DatatablesResponse.build(dataSet, criterias);
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getPaymentForm(ModelMap model){
        model.addAttribute("createOrUpdate", "Create");
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("paymentForm", new PaymentForm());
        return "payments/createOrUpdatePaymentForm";
    } 
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody HashMap processPaymentForm(@ModelAttribute("paymentForm") @Valid PaymentForm paymentForm,
                                                    BindingResult result, @RequestParam(value="update", required = false) Long id){
        System.out.println("FUCKKKKKKKKKKK");
        HashMap response = new HashMap();
        if(id != null)
            paymentForm.getPayment().setId(id);
        //form is valid with no transaction constraints
        if(!result.hasErrors())
            result = (BindingResult)paymentService.validate(paymentForm, result);
        if(!result.hasErrors()){
            try{
                response.put("result", paymentService.save(paymentForm));
            } catch(JpaOptimisticLockingFailureException e){
                result.reject("global","This record was modified by another user. Try refreshing the page.");
            } catch(Exception e){
                result.reject("global", e.getMessage());
            }
        } if (result.hasErrors()) {
            response.put("status","FAILURE");
            response.put("result",result.getAllErrors());
            return response;
        }
        System.out.println("HERE WTF");
        response.put("status","SUCCESS");
        return response;
    }

    @RequestMapping(value="/print-check", method=RequestMethod.POST)
    public @ResponseBody
    HashMap paymentIdsCheck(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
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

    @RequestMapping(value="/history", method=RequestMethod.POST)
    public String printHistory(ModelMap model, @ModelAttribute("checkboxes") Checkboxes checkboxes){
        List<Long> ids = new ArrayList();
        for(Long id: checkboxes.getCheckboxValues())
            ids.add(id);
        model.put("datasource", paymentService.paymentHistoryDataSource(ids));
        model.put("IS_HISTORY", true);
        model.put("format", "pdf");
        return "rpt_payment_history";
    }

    @RequestMapping(value="/fetchAccount", method=RequestMethod.POST)
    public @ResponseBody HashMap getaccountAndInvoiceData(@ModelAttribute("searchForm") @Valid SearchForm form, BindingResult result){
        System.out.println("FETCH ACCOUNT");
        HashMap response = new HashMap();
        Account account = null;
        if(!result.hasErrors())
            account = accountRepo.findByNumber(form.getAccountNumber());
        if(result.hasErrors() || account == null ) {
            result.rejectValue("accountNumber", "", "Account does not exists");
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        else{
            Invoice lastBill = invoiceRepo.findTopByAccountOrderByIdDesc(account);
            response.put("status", "SUCCESS");
            response.put("account", account);
            BigDecimal lastDue = (lastBill != null) ? lastBill.getNetCharge() : BigDecimal.ZERO;
            response.put("lastDue", lastDue);
        }
        return response;
    }
    
    @RequestMapping(value="/{paymentId}/check-can-edit")
    public @ResponseBody HashMap checkCanEdit(@PathVariable("paymentId") Long id){
        HashMap response = new HashMap();
        try{
            Payment payment  = paymentService.findPaymentById(id);
            Invoice lastestInvoice = invoiceRepo.findTopByAccountOrderByIdDesc(payment.getAccount());
            if(payment.getInvoice().getId().equals(lastestInvoice.getId())){
                response.put("status", "SUCCESS");
                response.put("lastDue", lastestInvoice.getNetCharge());
                response.put("payment", payment);
                return response;
            }
        }catch(Exception e){
            response.put("status", "FAILURE");
        }
        response.put("status", "FAILURE");
        return response;
    }
    @RequestMapping(value="/finalize-payments", method = RequestMethod.POST)
    public @ResponseBody HashMap finalizePayments(@ModelAttribute("addressForm") @Valid Address addressForm, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Address address = addressRepo.findByBrgyAndLocationCode(addressForm.getBrgy(), addressForm.getLocationCode());
            if(address != null) {
                response.put("result", paymentService.updateAccountsWithNoPayments(address));
                response.put("status", "SUCCESS");
            } else {
                result.rejectValue("brgy","","");
                result.rejectValue("locationCode","","");
                result.reject("global", "Address does not exists");
            }
        } if(result.hasErrors()) {
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
}