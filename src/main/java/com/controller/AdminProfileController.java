/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.AccountRepository;
import com.dao.springdatajpa.UserRepository;
import com.domain.User;
import com.domain.enums.AccountStatus;
import com.domain.enums.UserStatus;
import com.service.ReportService;
import com.service.SettingsService;
import com.service.SystemUserService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private ReportService reportService;
    private AccountRepository accountRepo;
    private SettingsService settingsService;

    @Autowired
    public AdminProfileController(PasswordEncoder encoder, SystemUserService userService, UserRepository userRepo,
                                  ReportService reportService, AccountRepository accountRepo, SettingsService settingsService){
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.userService = userService;
        this.reportService = reportService;
        this.accountRepo = accountRepo;
        this.settingsService = settingsService;
    }
    @RequestMapping(method=RequestMethod.GET)
    public String admin(ModelMap model){
        Long activeAccounts = accountRepo.countByStatus(AccountStatus.ACTIVE), warningAccounts = accountRepo.countByStatus(AccountStatus.WARNING),
                            inactiveAccounts = accountRepo.countByStatus(AccountStatus.INACTIVE);
        Integer activeUsers = userRepo.findByStatus(UserStatus.ACTIVE).size(), inactiveUsers = userRepo.findByStatus(UserStatus.INACTIVE).size();
        int month =  LocalDateTime.now().getMonthOfYear(), year = LocalDateTime.now().getYear();
        if(month == 1)
            year --;
        model.put("currentYear", year);
        model.put("activeUsers", activeUsers);
        model.put("inactiveUsers", inactiveUsers);
        model.put("activeAccounts", activeAccounts);
        model.put("warningAccounts", warningAccounts);
        model.put("inactiveAccounts", inactiveAccounts);
        model.put("settings", settingsService.getCurrentSettings());
        return "admin/admin";
    }

    @RequestMapping(value="/update-password", method=RequestMethod.POST)
    public @ResponseBody HashMap postEditPassword(@RequestParam Map<String, String> params){
        HashMap response = new HashMap(), fieldErrors = new HashMap();
        String newPassword = params.get("new"), currentPassword = params.get("current");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepo.findById(user.getId());
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
            try{
                userService.saveUser(user);
                response.put("status", "SUCCESS");
            }catch(Exception e) {
                fieldErrors.put("global", "An unexpected error occurred while saving the data. Please report it to the developer.");
                response.put("status","FAILURE");
            }
        }
        if(errors){
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
            user = userRepo.findById(user.getId());
            user.setFullName(fullName);
            user = userService.saveUser(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("fullName", user.getFullName());
            response.put("status", "SUCCESS");
        }
        return response;
    }
    @RequestMapping(value = "/get-chart-data", method = RequestMethod.POST)
    public @ResponseBody HashMap getChartData(@RequestParam("chart") Integer chartIndex){
        HashMap response = new HashMap();
        if(chartIndex != null){
            int year = LocalDateTime.now().getYear(), month = LocalDateTime.now().getMonthOfYear();
            if(month == 1)
                year --;
            HashMap result;
            if(chartIndex == 1) {
                result = reportService.getCollectionCollectiblesExpenseDataSource(year);
                response.put("result", result);
            }
            else if (chartIndex == 2) {
                result = reportService.getConsumptionDataSource(year);
                response.put("result", result);
            }
            else{
                response.put("status", "FAILURE");
                return response;
            }
            ArrayList<HashMap> datasets = (ArrayList<HashMap>) result.get("datasets");
            for(HashMap dataset: datasets){
                ArrayList data = (ArrayList)dataset.get("data");
                if(!data.isEmpty()){
                    response.put("status", "SUCCESS");
                    return response;
                }
            }
        }
        response.put("status", "FAILURE");
        return response;
    }
}