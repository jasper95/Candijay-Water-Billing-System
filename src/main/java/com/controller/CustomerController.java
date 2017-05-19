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
import com.forms.AccountForm;
import com.forms.CustomerForm;
import com.service.CustomerManagementService;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.service.FormOptionsService;
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
@RequestMapping("/admin/customers")
@SessionAttributes({"customerForm", "accountForm"})
public class CustomerController {

    private CustomerManagementService custService;
    private CustomerRepository customerRepo;
    private static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.customerForm";
    private AccountRepository accountRepo;
    private AddressRepository addressRepo;
    private DeviceRepository deviceRepo;
    private FormOptionsService formOptionsService;

    @Autowired
    public CustomerController(CustomerManagementService custService,
            CustomerRepository customerRepo, AccountRepository accountRepo, AddressRepository addressRepo, DeviceRepository deviceRepo,
              FormOptionsService formOptionsService) {
        this.custService = custService;
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.addressRepo = addressRepo;
        this.deviceRepo = deviceRepo;
        this.formOptionsService = formOptionsService;
    }
    
    @InitBinder({"customerForm", "accountForm"})
    public void initBinder(WebDataBinder binder){
        binder.setAllowedFields("customer.lastname", "customer.firstName", "customer.middleName",
                                    "customer.gender", "customer.timestamp", "customer.birthDate",
                                    "customer.familyMembersCount", "customer.contactNumber",
                                    "customer.occupation", "device.meterCode", "device.brand", "device.lastReading",
                                    "address.brgy", "account.purok");
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCustomerForm(ModelMap model) {
        HashMap<String, Collections> options = formOptionsService.getCustomerFormOptions();
        model.addAttribute("genderOptions", options.get("gender"));
        model.addAttribute("brgyOptions", options.get("brgy"));
        model.addAttribute("purokOptions", options.get("purok"));
        model.addAttribute("createOrUpdate", "Create");
        if(!model.containsAttribute(BINDING_RESULT_NAME))
            model.addAttribute("customerForm", new CustomerForm());
        return "customers/createOrUpdateCustomerForm";
    }
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String processCustomerForm(@ModelAttribute("customerForm") @Valid CustomerForm customerForm,
                                      BindingResult result, RedirectAttributes redirectAttributes,
                                      SessionStatus status, @RequestParam("duplicateMCToggle") boolean allowDuplicateMeterCode) {
        Customer customer = null;
        Address address = addressRepo.findByBrgy(customerForm.getAddress().getBrgy());
        customerForm.setAddress(address);
        String meterCode = customerForm.getDevice().getMeterCode().trim(), meterBrand = customerForm.getDevice().getBrand().trim();
        Integer lastReading = customerForm.getDevice().getLastReading();
        if(lastReading == null)
            result.rejectValue("device.lastReading", "", "This field is required");
        if(meterCode.isEmpty())
            result.rejectValue("device.meterCode","","This field is required");
        if(meterBrand.isEmpty())
            result.rejectValue("device.brand","","This field is required");
        if(!meterCode.isEmpty() && !allowDuplicateMeterCode && deviceRepo.countByMeterCode(meterCode) > 0)
            result.rejectValue("device.meterCode", "", "Metercode already exists");
        if(!result.hasErrors()){
            try{
                customer = custService.createCustomer(customerForm);
            }catch(Exception e) {
                result.reject("UnexpectedError");
            }
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:/admin/customers/new/";
        }
        status.setComplete();
        return "redirect:/admin/customers/"+customer.getId()+"/";
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String showCustomerList() {
        return "customers/customerList";
    }
    
    @RequestMapping(value="/{customerId}")
    public String viewCustomer(@PathVariable("customerId") Long id, ModelMap model){
        Customer customer = customerRepo.findOne(id);
        if ( customer == null){
            model.put("type", "Bad request URL");
            model.put("message", "Please avoid retrieving admin pages via URL");
            return "errors";
        }
        CustomerForm customerForm  = new CustomerForm();
        HashMap<String, Collections> options = formOptionsService.getCustomerFormOptions();
        customerForm.setCustomer(customer);
        AccountForm accountForm = new AccountForm();
        accountForm.setCustomerId(id);
        model.addAttribute("accountForm", accountForm);
        model.addAttribute("customerForm", customerForm);
        model.addAttribute("genderOptions", options.get("gender"));
        model.addAttribute("brgyOptions", options.get("brgy"));
        model.addAttribute("purokOptions", options.get("purok"));
        return "customers/viewCustomer";
    }
    
    @RequestMapping(value="/{customerId}/update", method=RequestMethod.GET)
    public String initUpdateForm(@PathVariable("customerId") Long id, ModelMap model){
        HashMap genderOptions = new HashMap<>();
        genderOptions.put('M', "Male");
        genderOptions.put('F', "Female");
        model.addAttribute("genderOptions", genderOptions);
        model.addAttribute("createOrUpdate", "Update");
        if (!model.containsAttribute(BINDING_RESULT_NAME)){
            Customer customer = customerRepo.findOne(id);
            if (customer == null){
                model.put("type", "Bad request URL");
                model.put("message", "Please avoid retrieving admin pages via URL");
                return "errors";
            }
            else{
                CustomerForm customerForm = new CustomerForm();
                customerForm.setCustomer(customer);
                model.addAttribute("customerForm", customerForm);
            }
        }
        return "customers/createOrUpdateCustomerForm";  
    }
    @RequestMapping(value="/{customerId}/update", method=RequestMethod.POST)
    public String processUpdate(@ModelAttribute("customerForm") @Valid CustomerForm customerForm,  
                                     BindingResult result, RedirectAttributes redirectAttributes,
                                     HttpServletRequest request, SessionStatus status, @PathVariable("customerId") Long id){
        if(!result.hasErrors()){
            try{
                custService.updateCustomer(customerForm);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("global", "The data were modified by another user. Please reload the page.");
            }catch(Exception e){
                result.reject("UnexpectedError");
            }
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:"+request.getRequestURI()+"/";
        }
        status.setComplete();
        redirectAttributes.addFlashAttribute("updateSuccess", 1);
        return "redirect:/admin/customers/"+customerForm.getCustomer().getId()+"/";
    }
    
    @RequestMapping("/{customer_id}/accounts")
    public @ResponseBody List<Account> getAllAccounts(@PathVariable("customer_id") Long id){
        return accountRepo.getByCustomer_Id(id);
    }
}