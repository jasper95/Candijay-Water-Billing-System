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
import com.dao.util.EnglishNumberToWords;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.forms.AccountForm;
import com.forms.Checkboxes;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * controller handler for accounts module
 * @author jasper
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
    private AddressRepository addressRepo;
    private FormOptionsService formOptionsService;
    private SettingsService settingsService;

    @Autowired
    public AccountController(CustomerManagementService custService, DataTableService dataTableService, 
            PaymentService paymentService, DeviceRepository deviceRepo, AccountRepository accountRepo, 
            AddressRepository addressRepo, FormOptionsService formOptionsService, SettingsService settingsService) {
        this.custService = custService;
        this.dataTableService = dataTableService;
        this.paymentService = paymentService;
        this.deviceRepo = deviceRepo;
        this.accountRepo = accountRepo;
        this.addressRepo = addressRepo;
        this.formOptionsService = formOptionsService;
        this.settingsService = settingsService;
    }

    /**
     *  form data binder configuration in accounts module.
     */
    @InitBinder({"accountForm", "deviceForm"})
    public void initBinder(WebDataBinder binder){
        binder.setAllowedFields("address.brgy", "account.purok", "meterCode", "brand",
                "device.meterCode", "device.brand");
    }

    /**
     *  accounts module index web-service handler.
     */
    @RequestMapping(method=RequestMethod.GET)
    public String showAll(ModelMap model){
        model.addAttribute("checkboxes", new Checkboxes());
        return "accounts/accountList";
    }

    /**
     *  creating new account web-service handler.
    */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody HashMap processAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,
                                     BindingResult result) {

        Address address = addressRepo.findByBrgy(accountForm.getAddress().getBrgy());
        HashMap response = new HashMap();
        accountForm.setAddress(address);
        // meter-code validation
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

    /**
     *  viewing an account web-service handler
     */
    @RequestMapping(value="/{accountNumber}")
    public String viewAccount(@PathVariable("accountNumber") String number, ModelMap model){
        Account account = accountRepo.findByNumber(number);
        //account number does not exists
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
        model.addAttribute("purokOptions", formOptions.get("purok"));
        return "accounts/viewAccount";
    }

    /**
     *  updating an account web-service handler.
     */
    @RequestMapping(value="{accountNumber}/update", method=RequestMethod.POST)
    public @ResponseBody HashMap processUpdateAccountForm(@ModelAttribute("accountForm") @Valid AccountForm accountForm,
                                     BindingResult result,@PathVariable("accountNumber") String number) {
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Address address = addressRepo.findByBrgy(accountForm.getAddress().getBrgy());
            accountForm.setAddress(address);
            try{
                Account account = accountRepo.findByNumber(number);
                account.setPurok(accountForm.getAccount().getPurok());
                accountForm.setAccount(account);
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

    /**
     *  account datatable web-service handler.
     */
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<Account> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Account> dataSet = dataTableService.findWithDataTableCriterias(criterias, Account.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    /**
     *  checking for accounts passed to this, qualified to print notice of disconnection web-service handler
     */
    @RequestMapping(value="/notice-of-disconnection-check", method=RequestMethod.POST)
    public @ResponseBody 
    HashMap deactivateCheck(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        List<Long> qualifiedList = new ArrayList();
        HashMap response = new HashMap();
        if(result.hasErrors())
            response.put("status","FAILURE");
        else{
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(account.getStatus().equals(AccountStatus.WARNING))
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

    /**
     *  deactivating accounts web-service handler.
     */
    @RequestMapping(value="/deactivate-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap deactivateAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(account.isStatusUpdated()) {
                    if (!success)
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

    /**
     *  activating accounts web-service handler.
     */
    @RequestMapping(value="/activate-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap activateAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(account.isStatusUpdated() && !account.getStatus().equals(AccountStatus.ACTIVE)){
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

    /**
     *  warning account web-service handler.
     */
    @RequestMapping(value="/warning-accounts", method=RequestMethod.POST)
    public @ResponseBody HashMap warningAccounts(@ModelAttribute("checkboxes") @Valid Checkboxes checkboxes, BindingResult result){
        boolean success = false;
        if(!result.hasErrors()){
            Settings currentSettings = settingsService.getCurrentSettings();
            for(Long id : checkboxes.getCheckboxValues()){
                Account account = accountRepo.findOne(id);
                if(account.isStatusUpdated() && paymentService.isAllowedToSetWarningToAccount(account, currentSettings.getDebtsAllowed())){
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

    /**
     *  retrieving account's devices web-service handler.
     */
    @RequestMapping(value="/{accountNumber}/devices", method=RequestMethod.GET)
    public @ResponseBody List<Device> getAllDevice(@PathVariable("accountNumber") String number){
        return deviceRepo.findByOwner(accountRepo.findByNumber(number));
    }

    /**
     *  creating a new device for account web-service handler.
     */
    @RequestMapping(value="/{accountNumber}/create-device", method=RequestMethod.POST)
    public @ResponseBody HashMap createDevice(@ModelAttribute("deviceForm") @Valid Device device, BindingResult result, @PathVariable("accountNumber") String number){
        HashMap response = new HashMap();
        if(device.getMeterCode().trim().isEmpty())
            result.rejectValue("meterCode", "", "This field is required");
        if(device.getBrand().trim().isEmpty())
            result.rejectValue("brand", "", "This field is required");
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

    /**
     *  editing a device of an account web-service handler.
     */
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
        if(device.getMeterCode().trim().isEmpty())
            result.rejectValue("meterCode", "", "This field is required");
        if(device.getBrand().trim().isEmpty())
            result.rejectValue("brand", "", "This field is required");
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

    /**
     *  activating a device of an account web-service handler.
     */
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

    /**
    *   generating printable notice of disconnection to accounts web-service handler.
    */
    @RequestMapping(value="/print-notice-of-disconnection", method=RequestMethod.POST)
    public String printNoticeOfDisconnection(ModelMap map, @ModelAttribute("checkboxes") Checkboxes checkboxes){
        List<Account> accounts = new ArrayList();
        for(Long id : checkboxes.getCheckboxValues()){
            Account account = accountRepo.findOne(id);
            if(account != null && account.getStatus().equals(AccountStatus.WARNING))
                accounts.add(account);
        }
        if(accounts.isEmpty()){
            map.put("type", "Invalid Parameter(s)");
            map.put("message", "None of the accounts is qualified to generate notice of disconnection");
            return "errors";
        }
        map.put("datasource", new JRBeanCollectionDataSource(accounts));
        map.put("format", "pdf");
        map.put("DEBTS_ALLOWED", EnglishNumberToWords.covertIntNumberToBisaya(settingsService.getCurrentSettings().getDebtsAllowed()));
        return "rpt_disconnection_notice";
    }

    /**
     *  Retrieving a device by id web-service handler
     */
    @RequestMapping(value="/find-device", method = RequestMethod.POST)
    public @ResponseBody Device find(@RequestParam("device_id") Long id){
        return deviceRepo.findOne(id);
    }
}