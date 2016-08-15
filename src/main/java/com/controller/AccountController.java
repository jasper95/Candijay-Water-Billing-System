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
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.forms.AccountForm;
import com.forms.Checkboxes;
import com.forms.CustomerForm;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Model;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Bert
 */
@Controller
@RequestMapping("/admin/accounts")
@SessionAttributes({"accountForm", "deviceForm"})
public class AccountController {
    
    private DeviceRepository deviceRepo;
    private CustomerManagementService custService;
    private DataTableService dataTableService;
    private PaymentService paymentService;
    private AccountRepository accountRepo;
    private CustomerRepository customerRepo;
    private AddressRepository addressRepo;
    private InvoicingService invoicingService;
    private FormOptionsService formOptionsService;
    private SettingsService settingsService;
    static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.accountForm";

    @Autowired
    public AccountController(CustomerManagementService custService, DataTableService dataTableService, 
            PaymentService paymentService, DeviceRepository deviceRepo, AccountRepository accountRepo, 
            CustomerRepository customerRepo, AddressRepository addressRepo, InvoicingService invoicingService,
             FormOptionsService formOptionsService, SettingsService settingsService) {
        this.custService = custService;
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.deviceRepo = deviceRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.invoicingService = invoicingService;
        this.formOptionsService = formOptionsService;
        this.settingsService = settingsService;
    }

    @InitBinder({"accountForm", "deviceForm"})
    public void initBinder(WebDataBinder binder){
        binder.setAllowedFields("address.brgy", "address.locationCode", "meterCode", "brand",
                "device.meterCode", "device.brand");
    }


    @RequestMapping(method=RequestMethod.GET)
    public String showAll(ModelMap model){
        model.addAttribute("checkboxes", new Checkboxes());
        return "accounts/accountList";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody HashMap processAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,
                                     BindingResult result) {

        Address address = addressRepo.findByBrgyAndLocationCode(accountForm.getAddress().getBrgy(), accountForm.getAddress().getLocationCode());
        HashMap response = new HashMap();
        if(address == null){
            result.rejectValue("address.locationCode", "locationCode", "Invalid Zone for Barangay");
        } else{
            accountForm.setAddress(address);
        }
        if(deviceRepo.findByMeterCode(accountForm.getDevice().getMeterCode().trim()) != null)
            result.rejectValue("device.meterCode", "meterCode", "Meter code already exists");
        if(!result.hasErrors()){
            response.put("result",custService.createAccount(accountForm));
            return response;
        }
        else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
            return response;
        }
    }
    
    @RequestMapping(value="/{accountNumber}")
    public String viewAccount(@PathVariable("accountNumber") String number, ModelMap model){
        Account account = accountRepo.findByNumber(number);
        if ( account == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin pages via URL");
            return "errors";
        }
        HashMap formOptions = formOptionsService.getCustomerFormOptions();
        AccountForm accountForm = new AccountForm();
        accountForm.setAccount(account);
        accountForm.setAddress(account.getAddress());
        accountForm.setCustomerId(account.getCustomer().getId());
        model.addAttribute("accountForm", accountForm);
        model.addAttribute("deviceForm", new Device());
        model.addAttribute("brgyOptions", formOptions.get("brgy"));
        model.addAttribute("zoneOptions", formOptions.get("zone"));
        return "accounts/viewAccount";
    }

    @RequestMapping(value="{accountNumber}/update", method=RequestMethod.POST)
    public @ResponseBody HashMap processUpdateAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,
                                     BindingResult result,@PathVariable("accountNumber") String number) {
        Address address = addressRepo.findByBrgyAndLocationCode(accountForm.getAddress().getBrgy(), accountForm.getAddress().getLocationCode());
        HashMap response = new HashMap();
        if(address == null){
            result.rejectValue("address.locationCode", "locationCode", "Invalid Zone for Barangay");
        } else{
            accountForm.setAddress(address);
        }
        if(!result.hasErrors()){
            try{
                accountForm.setAccount(accountRepo.findByNumber(number));
                response.put("result", custService.updateAccount(accountForm));
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("global","This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
            return response;
        }
        response.put("status", "SUCCESS");
        return response;
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
            Settings currentSettings = settingsService.getCurrentSettings();
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(paymentService.isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed())){
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
        if(deviceRepo.findByMeterCode(device.getMeterCode().trim()) != null)
            result.rejectValue("meterCode", "", "Metercode already exists!");
        if(!result.hasErrors()){
            custService.saveNewDevice(number, device);
            response.put("status", "SUCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
    @RequestMapping(value="/{device_id}/edit-device", method=RequestMethod.POST)
    public @ResponseBody HashMap updateDevice(@ModelAttribute("deviceForm") @Valid Device device, BindingResult result, @PathVariable("device_id") Long id) {
        HashMap response = new HashMap();
        Device origDevice = deviceRepo.findOne(id);
        String meterCode = device.getMeterCode().trim();
        if (origDevice == null)
            result.reject("Device does not exist");
        else if(deviceRepo.findByMeterCode(meterCode) != null && !origDevice.getMeterCode().equalsIgnoreCase(meterCode)) {
            result.rejectValue("meterCode", "", "Metercode already exists!");
        }
        if(!result.hasErrors()){
            custService.updateDevice(id, device);
            response.put("status", "SUCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;
    }
    @RequestMapping(value="/activate-device", method=RequestMethod.POST)
    public @ResponseBody HashMap activateDevice(@RequestParam("device_id") Long id){
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
            Settings currentSettings = settingsService.getCurrentSettings();
            if(paymentService.isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed())){
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
    @RequestMapping(value="/find-device", method = RequestMethod.POST)
    public @ResponseBody Device find(@RequestParam("device_id") Long id){
        return deviceRepo.findOne(id);
    }
}