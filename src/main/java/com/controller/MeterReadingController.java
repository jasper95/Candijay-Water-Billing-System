/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;


import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.DeviceRepository;
import com.dao.springdatajpa.MeterReadingRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.domain.Account;
import com.domain.Device;
import com.domain.MeterReading;
import com.domain.Schedule;
import com.domain.enums.AccountStatus;
import com.forms.MeterReadingForm;
import com.forms.SearchForm;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.response.json.FormValidationResponse;
import com.service.DataTableService;
import com.service.MeterReadingService;
import java.util.Calendar;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Bert
 */
@Controller
@RequestMapping(value="/admin/reading")
public class MeterReadingController {
 
    static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.meterReadingForm";
    private MeterReadingService mrService;
    private DataTableService dataTableService;
    private AccountRepository accountRepo;
    private DeviceRepository deviceRepo;
    private MeterReadingRepository mrRepo;
    private ScheduleRepository schedRepo;
    
    @Autowired
    public MeterReadingController(MeterReadingService mrService, DataTableService dataTableService, AccountRepository accountRepo,
            DeviceRepository deviceRepo, MeterReadingRepository mrRepo, ScheduleRepository schedRepo) {
        this.mrService = mrService;
        this.dataTableService = dataTableService;
        this.accountRepo = accountRepo;
        this.deviceRepo = deviceRepo;
        this.mrRepo = mrRepo;
        this.schedRepo = schedRepo;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String meterReadings(Map<String, Object> model){
        return "meterReading/meterReadingList";
    }
            
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String createOrUpdateMeterReading(ModelMap model){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Map yearOptions = new HashMap();
        for(int i = year; i > year-7; i--)
            yearOptions.put(i, i);
        model.addAttribute("yearOptions", yearOptions);
        model.addAttribute("createOrUpdate", "Create");
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("meterReadingForm", new MeterReadingForm());
        return "meterReading/createOrUpdateMeterReading";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody FormValidationResponse processCustomerForm(@ModelAttribute("meterReadingForm") @Valid MeterReadingForm meterReadingForm,  
                                     BindingResult result) {
        FormValidationResponse response = new FormValidationResponse();
        //form is valid with no transaction constraints
        if(!result.hasErrors()){
            Account account = accountRepo.findOne(meterReadingForm.getAccountId());
            MeterReading reading = mrService.findAccountLastMeterReading(account, 1);
            Schedule sched = schedRepo.findByMonthAndYear(meterReadingForm.getMeterReading().getSchedule().getMonth(), meterReadingForm.getMeterReading().getSchedule().getYear());
            Device device = deviceRepo.findByOwnerAndActive(account, true);
            meterReadingForm.getMeterReading().setConsumption(meterReadingForm.getMeterReading().getReadingValue() - device.getLastReading());
            if(sched != null){
                meterReadingForm.getMeterReading().setSchedule(sched);
                if(mrRepo.findByAccountAndSchedule(account, sched) != null)
                    result.reject("Duplicate meter reading for this user in this period." );
            }
            else
                meterReadingForm.getMeterReading().setSchedule(new Schedule(meterReadingForm.getMeterReading().getSchedule().getMonth(),meterReadingForm.getMeterReading().getSchedule().getYear()));
            if(reading != null && !mrService.isReadingPaid(reading))
                result.reject("The bill for the previous reading is unpaid.");            
            if(device.getLastReading().compareTo(meterReadingForm.getMeterReading().getReadingValue()) >= 0)
                result.reject("Invalid meter reading value");        
            if(!account.getStatus().equals(AccountStatus.ACTIVE))
                result.reject("You can add reading to ACTIVE accounts only");
        }
        //form is valid with transaction constraints
        if(!result.hasErrors()){
            try{
                mrService.saveMeterReading(meterReadingForm);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            response.setStatus("FAILURE");
            response.setResult(result.getAllErrors());
            return response;
        }
        response.setStatus("SUCCESS");
        return response;
    }
    @RequestMapping(value="/{readingId}/check-paid")
    public @ResponseBody FormValidationResponse updateMeterReading(@PathVariable("readingId") Long id){
        MeterReading reading = mrRepo.findOne(id);
        FormValidationResponse response = new FormValidationResponse();
        try{
            if(mrService.isReadingPaid(reading)){
                response.setStatus("FAILED");
                response.setResult(false);
                return response;
            }
        }catch(Exception e){
            response.setStatus("FAILED");
            response.setResult(false);
            return response;
        }
        response.setStatus("SUCCESS");
        response.setResult(true);
        return response;
    }
    
    @RequestMapping(value="/{readingId}/edit")
    public String editReading(ModelMap model, @PathVariable("readingId") Long id){
        MeterReading reading = mrRepo.findOne(id);
        if(reading == null || mrService.isReadingPaid(reading))
            return "error404";
        MeterReadingForm form = new MeterReadingForm();
        form.setAccountId(reading.getAccount().getId());
        form.setMeterReading(reading);
        SearchForm searchForm = new SearchForm();
        searchForm.setAccountNumber(reading.getAccount().getNumber());
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Map yearOptions = new HashMap();
        for(int i = year; i > year-7; i--)
            yearOptions.put(i, i);
        model.addAttribute("yearOptions", yearOptions);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("meterReadingForm", form);
        model.addAttribute("createOrUpdate", "Update");
        return "meterReading/createOrUpdateMeterReading";
    }
    @RequestMapping(value="/{readingId}/edit", method=RequestMethod.POST)
    public @ResponseBody FormValidationResponse processUpdate(@ModelAttribute("meterReadingForm") @Valid MeterReadingForm mForm,
                                                               BindingResult result, @PathVariable("readingId") Long id){
        FormValidationResponse response = new FormValidationResponse();
        MeterReading reading = null, formReading = null;
        Schedule newSchedule = null;
        //form is valid with no transaction constraints
        if(!result.hasErrors()){
            reading = mrRepo.findOne(id);
            formReading = mForm.getMeterReading();
            newSchedule = schedRepo.findByMonthAndYear(mForm.getMeterReading().getSchedule().getMonth(), mForm.getMeterReading().getSchedule().getYear());
            Account account = accountRepo.findOne(mForm.getAccountId());
            Device device = deviceRepo.findByOwnerAndActive(account, true);
            mForm.getMeterReading().setConsumption(mForm.getMeterReading().getReadingValue() - device.getLastReading());
            //check if reading is paid
            if(mrService.isReadingPaid(reading)){
                result.reject("Cannot edit a paid reading.");
            }
            //check if new schedule is valid
            if(newSchedule != null){
                if(!reading.getSchedule().getId().equals(newSchedule.getId())){
                    if( mrRepo.findByAccountAndSchedule(account, newSchedule) != null){
                        result.reject("Duplicate meter reading for this user in this period.");
                    } else mForm.getMeterReading().setSchedule(newSchedule);
                }
            } else{
                result.reject("Cannot use a period that is not used on existing records.");
            }
            //check if reading value is valid
            if(device.getLastReading().compareTo(reading.getReadingValue()) >= 0)
                result.reject("Invalid meter reading value");
            //check if account is active
            if(!account.getStatus().equals(AccountStatus.ACTIVE))
                result.reject("You can add reading to ACTIVE accounts only");
        }
        //form is valid with transcation constraints
        if(!result.hasErrors()){
            try{
                reading.setReadingValue(formReading.getReadingValue());
                reading.setSchedule(newSchedule);
                reading.setConsumption(formReading.getConsumption());
                mForm.setMeterReading(reading);
                mrService.saveMeterReading(mForm);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            response.setStatus("FAILURE");
            response.setResult(result.getAllErrors());
            return response;
        }
        response.setStatus("SUCCESS");
        return response;
                
    }
    
    @RequestMapping(value="/fetchAccount", method=RequestMethod.POST)
    @ResponseBody
    public HashMap fetchAccount(@ModelAttribute("searchForm") @Valid SearchForm searchForm, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            Account account = accountRepo.findByNumber(searchForm.getAccountNumber());
            response.put("account", account);
            if(account != null){
                if(searchForm.isCreate())
                    response.put("reading", mrService.findAccountLastMeterReading(account, 1));
                else response.put("reading", mrService.findAccountLastMeterReading(account, 2));
                response.put("device", deviceRepo.findByOwnerAndActive(account, true));
            }
        } else response.put("account", null);
        return response;         
    }
    
    @RequestMapping(value = "/datatable-search")
    public @ResponseBody
    DatatablesResponse<MeterReading> findAllForDataTablesFullSpring(@DatatablesParams DatatablesCriterias criterias) {
       DataSet<MeterReading> dataSet = dataTableService.findWithDataTableCriterias(criterias, MeterReading.class);
       return DatatablesResponse.build(dataSet, criterias);
    }
}