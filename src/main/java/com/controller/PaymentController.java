/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.*;
import com.domain.*;
import com.forms.BillDiscountForm;
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

import org.joda.time.LocalDateTime;
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

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getPaymentForm(ModelMap model){
        model.addAttribute("createOrUpdate", "Create");
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("paymentForm", new PaymentForm());
        model.addAttribute("billDiscountForm", new BillDiscountForm());
        return "payments/createOrUpdatePaymentForm";
    } 
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody HashMap saveNewPayment(@ModelAttribute("paymentForm") @Valid PaymentForm paymentForm,
                                                    BindingResult result){
        HashMap response = new HashMap();
        result = paymentService.validate(paymentForm, result);
        if(!result.hasErrors()){
            try{
                response.put("result", paymentService.save(paymentForm));
            } catch(Exception e){
                result.reject("global", "An unexpected error occurred while saving the data. Please report it to the developer.");
            }
        }
        if (result.hasErrors()) {
            response.put("status","FAILURE");
            response.put("result",result.getAllErrors());
            return response;
        }
        response.put("status","SUCCESS");
        return response;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody HashMap updatePayment(@ModelAttribute("paymentForm") @Valid PaymentForm form, BindingResult result,
                                               @RequestParam("update") Long id){
        HashMap response = new HashMap();
        form.getPayment().setId(id);
        result = paymentService.validate(form, result);
        if(!result.hasErrors()){
            try{
                response.put("result", paymentService.save(form));
            } catch (JpaOptimisticLockingFailureException e){
                response.put("global", "This record was modified by another user. Try reloading the form.");
            } catch(Exception e){
                result.reject("global", "An unexpected error occurred while saving the data. Please report it to the developer.");
            }
        }
        if (result.hasErrors()) {
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
            response.put("status", "SUCCESS");
            response.put("account", account);
        }
        return response;
    }
    
    @RequestMapping(value="/{paymentId}")
    public @ResponseBody HashMap checkCanEdit(@PathVariable("paymentId") Long id){
        HashMap response = new HashMap();
        Payment payment  = paymentRepo.findById(id);
        if (payment != null){
            response.put("payment", payment);
            response.put("status", "SUCCESS");
        }
        else response.put("status", "FAILURE");
        return response;
    }

    @RequestMapping(value="/finalize-payments", method = RequestMethod.POST)
    public @ResponseBody HashMap finalizePayments(@ModelAttribute("addressForm") @Valid Address addressForm, BindingResult result,
                                                    @RequestParam(value="confirmed", required = false) Long confirmed){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Address address = addressRepo.findByBrgy(addressForm.getBrgy());
            int day = LocalDateTime.now().getDayOfMonth();
            boolean canUpdate = false;
            if(day <= address.getDueDay()){
                if(confirmed != null && confirmed == 1)
                    canUpdate = true;
            } else canUpdate = true;
            if(canUpdate){
                response.put("result", paymentService.updateAccountsWithNoPayments(address));
                response.put("status", "SUCCESS");
            } else response.put("status", "PENDING");
            response.put("barangay", addressForm.getBrgy());
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }

    @RequestMapping(value="/recent-payments", method = RequestMethod.POST)
    public String printRecentPayments(@RequestParam("id") Long id, ModelMap model){
        Account account = accountRepo.findOne(id);
        if(account == null){
            model.put("type", "Requested page not found");
            model.put("message","The content does not exists or might have been deleted");
            return "errors";
        } else {
            model.put("ACCOUNT_NUMBER", account.getNumber());
            model.put("ACCOUNT_NAME", account.getCustomer().toString());
            model.put("ACCOUNT_ADDRESS", "Purok "+account.getPurok()+", "+account.getAddress().toString());
            model.put("format", "pdf");
            model.put("datasource", paymentService.getPreviousPaymentsDataSource(account));
            return "rpt_previous_payments";
        }

    }
}