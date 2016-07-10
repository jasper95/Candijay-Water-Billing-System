/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.domain.Account;
import com.domain.Invoice;
import com.domain.Payment;
import com.domain.enums.InvoiceStatus;
import com.forms.PaymentForm;
import com.forms.SearchForm;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.response.json.FormValidationResponse;
import com.service.CustomerManagementService;
import com.service.DataTableService;
import com.service.PaymentService;
import java.util.HashMap;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    @Autowired
    public PaymentController(DataTableService dataTableService, PaymentService paymentService, 
            AccountRepository accountRepo) {
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.accountRepo = accountRepo;
    }
   
    @RequestMapping(method=RequestMethod.GET)
    public String allPayments(){
        return "payments/paymentList";
    }
    
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<Payment> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
       DataSet<Payment> dataSet = dataTableService.findWithDataTableCriterias(criterias, Payment.class);
       return DatatablesResponse.build(dataSet, criterias);
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getPaymentForm(ModelMap model){
        model.addAttribute("createOrUpdate", "Create");
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("paymentForm", new PaymentForm());
        return "payments/createOrUpdatePaymentForm";
    } 
    
    @RequestMapping(value = "/process-payment", method = RequestMethod.POST)
    public @ResponseBody HashMap processPaymentForm(@ModelAttribute("paymentForm") @Valid PaymentForm paymentForm,
                                        BindingResult result, RedirectAttributes redirectAttributes){
        HashMap response = new HashMap();
        //form is valid with no transaction constraints
        if(!result.hasErrors())
            result = (BindingResult)paymentService.validate(paymentForm, result);
        if(!result.hasErrors()){
            try{
                Payment newPayment = paymentForm.getPayment();
                if(newPayment.getId() != null){
                    Payment oldPayment = paymentService.findPaymentById(paymentForm.getPayment().getId());
                    oldPayment.setAmountPaid(newPayment.getAmountPaid());
                    oldPayment.setDate(newPayment.getDate());
                    oldPayment.setDiscount(newPayment.getDiscount());
                    paymentForm.setPayment(oldPayment);
                }
                Payment instance = paymentService.save(paymentForm);
                response.put("result", instance);
            } catch(JpaOptimisticLockingFailureException e){
                result.reject("This record was modified by another user. Try refreshing the page.");
            }
        } if (result.hasErrors()) {
            response.put("status","FAILURE");
            response.put("result",result.getAllErrors());
            return response;
        }
        response.put("status","SUCCESS");
        return response;
    }
    
    @RequestMapping(value="/fetchAccount", method=RequestMethod.POST)
    public @ResponseBody HashMap getaccountAndInvoiceData(@ModelAttribute("searchForm") @Valid SearchForm form, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Account account = accountRepo.findByNumber(form.getAccountNumber());
            response.put("account", account);
            if(account != null)
                response.put("invoice", paymentService.findLatestBill(account));
        } else response.put("account", null);
        return response;
    }
    
    @RequestMapping(value="/{paymentId}/check-can-edit")
    public @ResponseBody ResponseEntity<HashMap> checkCanEdit(@PathVariable("paymentId") Long id){
        HashMap<String, String> response = new HashMap();
        Payment payment = paymentService.findPaymentById(id);
        if(paymentService.canEdit(payment)){
            response.put("status", "SUCCESS");
        } else {
            response.put("status", "FAILED");
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @RequestMapping(value="/{paymentId}/edit")
    public String editReading(ModelMap model, @PathVariable("paymentId") Long id){
        Payment payment = paymentService.findPaymentById(id);
        if(payment == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin pages via URL");
            return "errors";
        }
        if(!paymentService.canEdit(payment)){
            model.put("type", "Access Denied");
            model.put("message", "You are not allowed to edit this payment");
            return "errors";
        }   
        PaymentForm form = new PaymentForm();
        form.setAccountId(payment.getAccount().getId());
        form.setPayment(payment);
        SearchForm searchForm = new SearchForm();
        searchForm.setAccountNumber(payment.getAccount().getNumber());
        model.addAttribute("paymentForm", form);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("createOrUpdate", "Update");
        return "payments/createOrUpdatePaymentForm";
    }
   
}