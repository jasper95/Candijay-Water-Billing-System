/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.*;
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
import com.service.MeterReadingService;
import com.service.PaymentService;

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
    private MeterReadingService mrService;
    private PaymentRepository paymentRepo;
    
    @Autowired
    public PaymentController(DataTableService dataTableService, PaymentService paymentService, 
                             AccountRepository accountRepo, InvoiceRepository invoiceRepo, FormOptionsService formOptionsService,
                             AddressRepository addressRepo, MeterReadingService mrService, PaymentRepository paymentRepo) {
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.accountRepo = accountRepo;
        this.invoiceRepo = invoiceRepo;
        this.formOptionsService = formOptionsService;
        this.addressRepo = addressRepo;
        this.mrService = mrService;
        this.paymentRepo = paymentRepo;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String allPayments(ModelMap model){
        model.addAttribute("paymentForm", new PaymentForm());
        model.addAttribute("checkboxes", new Checkboxes());
        HashMap finalizePaymentFormOptions = formOptionsService.getCustomerFormOptions();
        model.addAttribute("brgyOptions", finalizePaymentFormOptions.get("brgy"));
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
            Invoice lastBill = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(account);
            response.put("status", "SUCCESS");
            response.put("account", account);
        }
        return response;
    }
    
    @RequestMapping(value="/{paymentId}/check-can-edit")
    public @ResponseBody HashMap checkCanEdit(@PathVariable("paymentId") Long id){
        HashMap response = new HashMap();
        try{
            Payment payment  = paymentRepo.findById(id);
            Invoice lastestInvoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(payment.getAccount());
            if(payment.getInvoice().getId().equals(lastestInvoice.getId())){
                response.put("status", "SUCCESS");
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
            Address address = addressRepo.findByBrgy(addressForm.getBrgy());
            List<Address> list = new ArrayList();
            list.add(address);
            try{
                if(mrService.isDoneReadingAddressIn(list)){
                    response.put("result", paymentService.updateAccountsWithNoPayments(address));
                    response.put("status", "SUCCESS");
                } else result.reject("global", "Not finished reading for this barangay");
            }catch(Exception e){
                result.reject("global", e.getMessage());
            }
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
}