/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;


import com.dao.springdatajpa.*;
import com.domain.*;
import com.domain.enums.AccountStatus;
import com.domain.enums.InvoiceStatus;
import com.forms.AccountabilityReportForm;
import com.forms.BillDiscountForm;
import com.forms.MeterReadingForm;
import com.forms.SearchForm;

import com.service.FormOptionsService;
import com.service.InvoicingService;
import com.service.MeterReadingService;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Bert
 */
@Controller
@RequestMapping(value="/admin/reading")
public class MeterReadingController {

    private MeterReadingService mrService;
    private AccountRepository accountRepo;
    private DeviceRepository deviceRepo;
    private MeterReadingRepository mrRepo;
    private ScheduleRepository schedRepo;
    private FormOptionsService formOptionsService;
    private InvoiceRepository invoiceRepo;
    private InvoicingService invoiceService;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setAllowedFields("meterReading.readingValue", "meterReading.schedule.month", "meterReading.schedule.year",
                                "meterReading.version", "accountId","accountNumber", "billId", "discount");
    }

    @Autowired
    public MeterReadingController(MeterReadingService mrService, AccountRepository accountRepo, InvoicingService invoiceService,
            DeviceRepository deviceRepo, MeterReadingRepository mrRepo, ScheduleRepository schedRepo,
              FormOptionsService formOptionsService, InvoiceRepository invoiceRepo) {
        this.mrService = mrService;
        this.accountRepo = accountRepo;
        this.deviceRepo = deviceRepo;
        this.mrRepo = mrRepo;
        this.schedRepo = schedRepo;
        this.formOptionsService = formOptionsService;
        this.invoiceRepo = invoiceRepo;
        this.invoiceService = invoiceService;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String meterReadings(ModelMap model){
        HashMap<String, Collections> options = formOptionsService.getMeterReadingFormOptions();
        model.addAttribute("yearOptions", options.get("year"));
        model.addAttribute("monthOptions", options.get("month"));
        model.addAttribute("meterReadingForm", new MeterReadingForm());
        model.addAttribute("addressForm", new AccountabilityReportForm());
        model.addAttribute("zoneOptions", options.get("zone"));
        model.addAttribute("brgyOptions", options.get("brgy"));
        return "meterReading/meterReadingList";
    }


    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String createOrUpdateMeterReading(ModelMap model){
        HashMap<String, Collections> options = formOptionsService.getMeterReadingFormOptions();
        model.addAttribute("yearOptions", options.get("year"));
        model.addAttribute("monthOptions", options.get("month"));
        model.addAttribute("createOrUpdate", "Create");
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("meterReadingForm", new MeterReadingForm());
        model.addAttribute("addressForm", new AccountabilityReportForm());
        model.addAttribute("zoneOptions", options.get("zone"));
        model.addAttribute("brgyOptions", options.get("brgy"));
        model.addAttribute("billDiscountForm", new BillDiscountForm());
        return "meterReading/createOrUpdateMeterReading";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody HashMap processCustomerForm(@ModelAttribute("meterReadingForm") @Valid MeterReadingForm meterReadingForm,
                                     BindingResult result) {
        HashMap response = new HashMap();
        //form is valid with no transaction constraints
        if(!result.hasErrors()){
            Account account = accountRepo.findOne(meterReadingForm.getAccountId());
            Schedule sched = schedRepo.findByMonthAndYear(meterReadingForm.getMeterReading().getSchedule().getMonth(), meterReadingForm.getMeterReading().getSchedule().getYear());
            Device device = deviceRepo.findByOwnerAndActive(account, true);
            meterReadingForm.getMeterReading().setConsumption(meterReadingForm.getMeterReading().getReadingValue() - device.getLastReading());
            if(sched != null){
                meterReadingForm.getMeterReading().setSchedule(sched);
                if(mrRepo.findByAccountAndSchedule(account, sched) != null) {
                    result.rejectValue("meterReading.schedule.month", "", "");
                    result.rejectValue("meterReading.schedule.year", "", "");
                    result.reject("global", "Duplicate meter reading for this user in this period.");
                }
            }
            else
                meterReadingForm.getMeterReading().setSchedule(new Schedule(meterReadingForm.getMeterReading().getSchedule().getMonth(),meterReadingForm.getMeterReading().getSchedule().getYear()));
            if(!account.isStatusUpdated())
                result.reject("global", "This account has not paid its previous bill. Please finalize the payment for "+account.getAddress().getBrgy()+", Zone "+account.getAddress().getLocationCode()+" before creating this reading.");
            if(device.getLastReading().compareTo(meterReadingForm.getMeterReading().getReadingValue()) > 0)
                result.rejectValue("meterReading.readingValue", "", "Invalid meter reading value");
            if(!account.getStatus().equals(AccountStatus.ACTIVE))
                result.reject("global", "You can add reading to ACTIVE accounts only");
        }
        //form is valid with transaction constraints
        if(!result.hasErrors()) {
            try{
                response.put("result", mrService.saveMeterReading(meterReadingForm));
            } catch(Exception e){
                result.reject("global", "An unexpected error occurred while saving the data. Please report it to the developer.");
            }
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
            return response;
        }
        response.put("status","SUCCESS");
        return response;
    }

    @RequestMapping(value="/{readingId}/check-can-edit")
    public @ResponseBody HashMap updateMeterReading(@PathVariable("readingId") Long id){
        MeterReading reading = mrRepo.findById(id), lastMeterReading = mrService.findAccountLastMeterReading(reading.getAccount());
        boolean invoiceIsDiscounted = reading.getInvoice().getDiscount().compareTo(BigDecimal.ZERO) > 0;
        HashMap response = new HashMap();
        if(!reading.equals(lastMeterReading)){
            response.put("status", "FAILED");
            response.put("message", "You can only edit account\'s latest reading");
        } else if (mrService.isReadingPaid(reading)){
            response.put("status", "FAILED");
            response.put("message", "You can only edit UNPAID reading.");
        } else if (invoiceIsDiscounted){
            response.put("status", "FAILED");
            response.put("message", "Cannot edit reading with bill discount greater than zero.");
        } else {
            response.put("status", "SUCCESS");
            response.put("reading", reading);
            int lastReading = (lastMeterReading != null) ? lastMeterReading.getReadingValue() - lastMeterReading.getConsumption(): 0;
            response.put("last_reading",  lastReading);
        }
        return response;
    }

    @RequestMapping(value="/delete/{id}")
    public @ResponseBody HashMap deleteReading(@PathVariable("id") Long id){
        HashMap response = new HashMap();
        if(id != null && mrService.deleteReading(id))
            response.put("status", "SUCCESS");
        else response.put("status", "FAILURE");
        return response;
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public @ResponseBody HashMap processUpdate(@ModelAttribute("meterReadingForm") @Valid MeterReadingForm mForm,
                                               BindingResult result, @RequestParam("readingId") Long id){
        HashMap response = new HashMap();
        MeterReading reading = null, formReading = null;
        Schedule newSchedule = null;
        //form is valid with basic constraints (empty, null, etc)
        if(!result.hasErrors()){
            reading = mrRepo.findOne(id);
            formReading = mForm.getMeterReading();
            newSchedule = schedRepo.findByMonthAndYear(mForm.getMeterReading().getSchedule().getMonth(), mForm.getMeterReading().getSchedule().getYear());
            Account account = accountRepo.findOne(mForm.getAccountId());
            MeterReading lastMeterReading = mrService.findAccountLastMeterReading(account);
            Integer lastReading = (lastMeterReading != null) ? lastMeterReading.getReadingValue() - lastMeterReading.getConsumption() : 0;
            mForm.getMeterReading().setConsumption(mForm.getMeterReading().getReadingValue() - lastReading);
            //check if reading is paid
            if(mrService.isReadingPaid(reading)){
                result.reject("global","Cannot edit a paid reading.");
            }
            //check if new schedule is valid
            if(newSchedule != null){
                if(!reading.getSchedule().getId().equals(newSchedule.getId())){
                    if( mrRepo.findByAccountAndSchedule(account, newSchedule) != null){
                        result.rejectValue("meterReading.schedule.month", "", "");
                        result.rejectValue("meterReading.schedule.year", "", "");
                        result.reject("global", "Duplicate meter reading for this user in this period.");
                    } else mForm.getMeterReading().setSchedule(newSchedule);
                }
            } else newSchedule = mForm.getMeterReading().getSchedule();
            //check if reading value is valid
            if(lastReading.compareTo(formReading.getReadingValue()) > 0)
                result.rejectValue("meterReading.readingValue", "", "Invalid meter reading value");
        }
        //form is valid with business constraints
        if(!result.hasErrors()){
            try{
                reading.setReadingValue(formReading.getReadingValue());
                reading.setSchedule(newSchedule);
                reading.setConsumption(formReading.getConsumption());
                reading.setVersion(formReading.getVersion());
                mForm.setMeterReading(reading);
                response.put("result", mrService.saveMeterReading(mForm));
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("global", "This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            response.put("status","FAILURE");
            response.put("result", result.getAllErrors());
            return response;
        }
        response.put("status","SUCCESS");
        return response;
    }
    
    @RequestMapping(value="/fetchAccount", method=RequestMethod.POST)
    @ResponseBody
    public HashMap fetchAccount(@ModelAttribute("searchForm") @Valid SearchForm searchForm, BindingResult result){
        HashMap response = new HashMap();
        Account account = null;
        Device device = null;
        if(!result.hasErrors()) {
            account = accountRepo.findByNumber(searchForm.getAccountNumber());
            device = deviceRepo.findByOwnerAndActive(account, true);
        }
        if(result.hasErrors() || account == null || device == null) {
            if(account == null)
                result.reject("global", "Account does not exists");
            else if(device == null)
                result.reject("global", "Account has no active device.");
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        else{
            response.put("status", "SUCCESS");
            response.put("account", account);
            response.put("last_reading", device.getLastReading());
        }
        return response;
    }

    @RequestMapping(value="/edit-discount/{id}")
    public @ResponseBody HashMap findBill(@PathVariable("id") Long id){
        Invoice invoice = invoiceRepo.findById(id),
                latestInvoice = invoiceRepo.findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(invoice.getAccount());
        HashMap response = new HashMap();
        if(invoice != null && invoice.equals(latestInvoice) && (invoice.getStatus().equals(InvoiceStatus.UNPAID) || invoice.getStatus().equals(InvoiceStatus.DEBT)) ) {
            response.put("invoice", invoice);
            response.put("status", "SUCCESS");
        } else response.put("status", "FAILURE");
        return response;
    }

    @RequestMapping(value = "/update-discount", method = RequestMethod.POST)
    public @ResponseBody HashMap updateDiscount(@ModelAttribute("billDiscountForm") @Valid BillDiscountForm form, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Invoice invoice = invoiceRepo.findOne(form.getBillId());
            if(form.getDiscount().compareTo(invoice.getNetCharge()) > 0)
                result.rejectValue("discount", "", "Invalid discount");
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        } else {
            response.put("invoice", invoiceService.updateDiscount(form));
            response.put("status", "SUCCESS");
        }
        return response;
    }
}