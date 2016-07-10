/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.AddressRepository;
import com.dao.springdatajpa.CustomerRepository;
import com.dao.springdatajpa.DeviceRepository;
import com.domain.Account;
import com.domain.Address;
import com.domain.Customer;
import com.domain.Device;
import com.domain.enums.AccountStatus;
import com.forms.AccountForm;
import com.forms.Checkboxes;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.CustomerManagementService;
import com.service.DataTableService;
import com.service.InvoicingService;
import com.service.PaymentService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Bert
 */
@Controller
@RequestMapping("/admin/accounts")
@SessionAttributes("accountForm")
public class AccountController {
    
    private DeviceRepository deviceRepo;
    private CustomerManagementService custService;
    private DataTableService dataTableService;
    private PaymentService paymentService;
    private AccountRepository accountRepo;
    private CustomerRepository customerRepo;
    private AddressRepository addressRepo;
    private InvoicingService invoicingService;
    static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.accountForm";

    @Autowired
    public AccountController(CustomerManagementService custService, DataTableService dataTableService, 
            PaymentService paymentService, DeviceRepository deviceRepo, AccountRepository accountRepo, 
            CustomerRepository customerRepo, AddressRepository addressRepo, InvoicingService invoicingService) {
        this.custService = custService;
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.deviceRepo = deviceRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.invoicingService = invoicingService; 
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String showAll(ModelMap model){
        model.addAttribute("checkboxes", new Checkboxes());
        return "accounts/accountList";
    }
    
    @RequestMapping(value="/{customer_id}/new", method=RequestMethod.GET)
    public String initAccountForm(@PathVariable("customer_id") Long id, ModelMap model) {
        Customer customer = customerRepo.findOne(id);
        if (customer == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin pages via URL");
            return "errors";
        }
        model.addAttribute("customer", customer);
        if(!model.containsAttribute(BINDING_RESULT_NAME)){
            AccountForm accountForm = new AccountForm();
            accountForm.setCustomerId(id);
            model.addAttribute("accountForm", accountForm);
        }
        return "accounts/createOrUpdateAccountForm";
    }
    
    @RequestMapping(value = "/{customer_id}/new", method = RequestMethod.POST)
    public String processAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,  
                                     BindingResult result,@PathVariable("customer_id") Long id,
                                     RedirectAttributes redirectAttributes,
                                     SessionStatus status) {
        Account account=null;
        Address address = addressRepo.findByBrgyAndLocationCode(accountForm.getAddress().getBrgy(), accountForm.getAddress().getLocationCode());
        if(address == null){
            result.rejectValue("address.locationCode", "", "Invalid Zone for Barangay");
        } else{
            accountForm.setAddress(address);
        }
        if(deviceRepo.findByMeterCode(accountForm.getDevice().getMeterCode()) != null)
            result.rejectValue("device.meterCode", "", "Meter code already exists");
        if(!result.hasErrors()){
            Account newAccount = new Account();
            String number = address.getAddressGroup().getAccountPrefix() + "-" + String.format("%05d", address.getAddressGroup().getAccountsCount());
            newAccount.setNumber(number);
            accountForm.setAccount(newAccount);
            try{
                account = custService.save(accountForm);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("","This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:/admin/accounts/"+id+"/new";
        }
        status.setComplete();
        return "redirect:/admin/accounts/"+account.getNumber();
    }
    
    @RequestMapping(value="/{accountNumber}")
    public String viewAccount(@PathVariable("accountNumber") String number, ModelMap model){
        Account account = accountRepo.findByNumber(number);
        if ( account == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin pages via URL");
            return "errors";
        }
        model.addAttribute("account", account);
        model.addAttribute("deviceForm", new Device());
        return "accounts/viewAccount";
    }
    
    @RequestMapping(value="/{accountNumber}/update")
    public String updateAccount(@PathVariable("accountNumber") String number, ModelMap model){
        Account account = accountRepo.findByNumber(number);
        if ( account == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin page via URL");
            return "errors";
        } else {
            AccountForm accountForm = new AccountForm();
            accountForm.setAccount(account);
            accountForm.setDevice(deviceRepo.findByOwnerAndActive(account, true));
            accountForm.setAddress(account.getAddress());
            accountForm.setCustomerId(account.getCustomer().getId());
            model.addAttribute("customer", account.getCustomer());
            model.addAttribute("accountForm", accountForm);
            return "accounts/createOrUpdateAccountForm";
        }
    }
    
    @RequestMapping(value="/{accountNumber}/update", method=RequestMethod.POST)
    public String processUpdateAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,  
                                     BindingResult result,@PathVariable("accountNumber") String number,
                                     RedirectAttributes redirectAttributes,
                                     SessionStatus status) {
        if(!result.hasErrors()){
            try{
                custService.save(accountForm);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("","This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:/admin/accounts/"+number+"/update";
        }
        status.setComplete();
        redirectAttributes.addFlashAttribute("updateSuccess", 1);
        return "redirect:/admin/accounts/"+number;
    }
    
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<Account> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Account> dataSet = dataTableService.findWithDataTableCriterias(criterias, Account.class);
        return DatatablesResponse.build(dataSet, criterias);
    }
    
    @RequestMapping(value="/deactivate-check", method=RequestMethod.POST)
    public @ResponseBody 
    HashMap deactivateCheck(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        List<Long> qualifiedList = new ArrayList();
        HashMap response = new HashMap();
        if(result.hasErrors())
            response.put("status","FAILURE");
        else{
            for(Long id : checkboxes.getCheckboxValues()){
                if(accountRepo.findOne(id).getStatus().equals(AccountStatus.WARNING))
                    qualifiedList.add(id);
            }
            if(!qualifiedList.isEmpty()){
                response.put("status", "SUCCESS");
                checkboxes.setCheckboxValues(qualifiedList);
                response.put("result", checkboxes);
            } else {
                response.put("status", "FAILURE");
            }
        }
        return response;
    }    
    
    @RequestMapping(value="/deactivate-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap deactivateAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(account.getStatus().equals(AccountStatus.WARNING)){
                    if(!success)
                        success = true;
                    custService.changeAccountStatus(account, AccountStatus.INACTIVE);
                }
            }
        }
        HashMap response = new HashMap();
        if(success)
            response.put("status", "SUCCESS");
        else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/activate-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap activateAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(!account.getStatus().equals(AccountStatus.ACTIVE)){
                    if(!success)
                        success = true;
                    custService.changeAccountStatus(account, AccountStatus.ACTIVE);
                }
            }
        }
        HashMap response = new HashMap();
        if(success)
            response.put("status", "SUCCESS");
        else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/warning-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap warningAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(paymentService.isAllowedToSetWarningToAccount(account)){
                    if(!success)
                        success = true;
                    custService.changeAccountStatus(account, AccountStatus.WARNING);
                }
            }
        }
        HashMap response = new HashMap();
        if(success)
            response.put("status", "SUCCESS");
        else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/{accountNumber}/devices", method=RequestMethod.GET)
    public @ResponseBody List<Device> getAllDevice(@PathVariable("accountNumber") String number){
        return deviceRepo.findByOwner(accountRepo.findByNumber(number));
    }
    
    @RequestMapping(value="/{accountNumber}/create-device", method=RequestMethod.POST)
    public @ResponseBody HashMap createDevice(@ModelAttribute("deviceForm") @Valid Device device, BindingResult result, @PathVariable("accountNumber") String number){
        HashMap response = new HashMap();
        if(deviceRepo.findByMeterCode(device.getMeterCode()) != null)
            result.rejectValue("meterCode", "", "Metercode already exists!");
        if(!result.hasErrors()){
            custService.saveDevice(number, device);
            response.put("status", "SUCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("errors", result.getAllErrors());
        }
        return response;
    }
    @RequestMapping(value="/activate-device/{device_id}", method=RequestMethod.POST)
    public @ResponseBody HashMap activateDevice(@PathVariable("device_id") Long id){
        HashMap response = new HashMap();
        Device device = deviceRepo.findOne(id);
        if(device != null){
            custService.activateDevice(device);
            response.put("status", "SUCCESS");
        } else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/print-notice-of-disconnection", method=RequestMethod.POST)
    public String printNoticeOfDisconnection(ModelMap map, @ModelAttribute("checkboxes") Checkboxes checkboxes){
        List<Account> accounts = new ArrayList();
        for(Long id : checkboxes.getCheckboxValues()){
            Account account = accountRepo.findOne(id);
            if(account != null && account.getStatus().equals(AccountStatus.WARNING))
                accounts.add(account);
        }
        if(accounts.isEmpty()){
            map.put("type", "Invalid URL Parameter(s)");
            map.put("message", "None of the accounts is allowed to give notice of disconnection");
            return "errors";
        }
        map.put("datasource", invoicingService.getDisconnectionNoticeDataSource(accounts));
        map.put("format", "pdf");
        return "rpt_disconnection_notice";
    }
    @RequestMapping(value="/warning", method=RequestMethod.POST)
    public @ResponseBody HashMap warningAccount(@RequestParam("accountId") Long id){
        HashMap response = new HashMap();
        Account account = accountRepo.findOne(id);
        if(account != null){
            if(paymentService.isAllowedToSetWarningToAccount(account)){
                custService.changeAccountStatus(account, AccountStatus.WARNING);
                response.put("status", "SUCCESS");
            } else response.put("status", "FAILURE");
        } else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/activate", method=RequestMethod.POST)
    public @ResponseBody HashMap activateAccount(@RequestParam("accountId") Long id){
        HashMap response = new HashMap();
        Account account = accountRepo.findOne(id);
        if(account != null){
            if(!account.getStatus().equals(AccountStatus.ACTIVE)){
                custService.changeAccountStatus(accountRepo.findOne(id), AccountStatus.ACTIVE);
                response.put("status", "SUCCESS");
            } else response.put("status", "FAILURE");
        }else response.put("status", "FAILURE");
        return response;
    }
    
    @RequestMapping(value="/deactivate", method=RequestMethod.POST)
    public @ResponseBody HashMap deactivateAccount(@RequestParam("accountId") Long id){
        HashMap response = new HashMap();
        Account account = accountRepo.findOne(id);
        if(account != null){
            if(account.getStatus().equals(AccountStatus.WARNING)){
                custService.changeAccountStatus(accountRepo.findOne(id), AccountStatus.INACTIVE);
                response.put("status", "SUCCESS");
            } else response.put("status", "FAILURE");
        }else response.put("status", "FAILURE");
        return response;
    }
}