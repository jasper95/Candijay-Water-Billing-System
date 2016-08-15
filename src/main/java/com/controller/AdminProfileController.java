/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.dao.springdatajpa.UserRepository;
import com.domain.Account;
import com.domain.User;
import com.domain.enums.AccountStatus;
import com.domain.enums.UserStatus;
import com.service.CustomerManagementService;
import com.service.InvoicingService;
import com.service.SystemUserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Bert
 */
@RequestMapping("/admin")
@Controller
public class AdminProfileController {
    

    private PasswordEncoder encoder;
    private SystemUserService userService;
    private UserRepository userRepo;
    private InvoicingService invoicingService;
    private AccountRepository accountRepo;
    private CustomerManagementService custService;
    private ScheduleRepository schedRepo;
    @Autowired
    public AdminProfileController(PasswordEncoder encoder, SystemUserService userService, UserRepository userRepo,
                                  InvoicingService invoicingService, AccountRepository accountRepo, CustomerManagementService custService,
                                  ScheduleRepository schedRepo){
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.userService = userService;
        this.invoicingService = invoicingService;
        this.accountRepo = accountRepo;
        this.custService = custService;
        this.schedRepo = schedRepo;
    }
    @RequestMapping(method=RequestMethod.GET)
    public String admin(ModelMap model){
        Integer activeAccounts = custService.getAllActiveAccounts().size(), warningAccounts = accountRepo.findByStatus(AccountStatus.WARNING).size(),
                            inactiveAccounts = accountRepo.findByStatus(AccountStatus.INACTIVE).size();
        Integer activeUsers = userRepo.findByStatus(UserStatus.ACTIVE).size(), inactiveUsers = userRepo.findByStatus(UserStatus.INACTIVE).size();
        return "admin/admin";
    }

    @RequestMapping(value="/update-password", method=RequestMethod.POST)
    public @ResponseBody HashMap postEditPassword(@RequestParam Map<String, String> params){
        HashMap response = new HashMap(), fieldErrors = new HashMap();
        String newPassword = params.get("new"), currentPassword = params.get("current");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepo.findOne(user.getId());
        boolean errors = false;
        if(newPassword == null || newPassword.trim().isEmpty()){
            fieldErrors.put("new", "Invalid new password");
            errors = true;
        }
        if(!encoder.matches(currentPassword, user.getPassword())){
            fieldErrors.put("current", "Incorrect current password");
            errors =  true;
        }
        if(!errors){
            user.setPassword(encoder.encode(newPassword));
            userService.saveUser(user);
            response.put("status", "SUCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("errors", fieldErrors);
        }
        return response;
    }
    
    @RequestMapping(value="/update-name", method=RequestMethod.POST)
    public @ResponseBody HashMap postEditName(@RequestParam Map<String, String> params){
        HashMap response = new HashMap();
        String fullName = params.get("fullName");
        if(fullName == null || fullName.trim().isEmpty()){
            response.put("status", "FAILURE");
            response.put("reason", "Invalid Input");
        }
        else{
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userRepo.findOne(user.getId());
            user.setFullName(fullName);
            response.put("fullName", userService.saveUser(user).getFullName());
            response.put("status", "SUCCESS");
        }
        return response;
    }
    @RequestMapping(value = "/get-chart-data", method = RequestMethod.POST)
    public @ResponseBody HashMap getChartData(@RequestParam("chart") Integer chartIndex){
        HashMap response = new HashMap();
        if(chartIndex != null){
            int year = LocalDateTime.now().getYear();
            if(chartIndex == 1)
                response.put("result", invoicingService.getCollectionCollectiblesExpenseDataSource(year));
            else if (chartIndex == 2)
                response.put("result", invoicingService.getConsumptionDataSource(year));
            else{
                response.put("status", "FAILURE");
            }
            response.put("status", "SUCCESS");
            return response;
        }
        response.put("status", "FAILURE");
        return response;
    }
}