/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.domain.User;
import com.service.SystemUserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Bert
 */
@RequestMapping("/admin")
@Controller
@SessionAttributes("user")
public class AdminProfileController {
    
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private SystemUserService userService;
    @RequestMapping(method=RequestMethod.GET)
    public String admin(){
        //User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "admin";
    }
    @RequestMapping(value="/update-profile",method=RequestMethod.GET)
    public String resetAdmin(ModelMap model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "admin/resetProfile";
    }
    
    @RequestMapping(value="/update-password", method=RequestMethod.POST)
    public @ResponseBody HashMap postEditPassword(@RequestParam Map<String, String> params){
        HashMap response = new HashMap();
        ArrayList<String> result = new ArrayList();
        String newPassword = params.get("new"), currentPassword = params.get("current");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean errors = false;
        if(newPassword == null || newPassword.trim().isEmpty()){
            result.add("Invalid new password");
            errors = true;
        }
        if(!encoder.matches(currentPassword, user.getPassword())){
            errors =  true;
            result.add("Incorrect current password");
        }
        if(!errors){
            user.setPassword(newPassword);
            userService.saveUser(user);
            response.put("status", "SUCCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("errors", result);
        }
        return response;
    }
    
    @RequestMapping(value="/update-name", method=RequestMethod.POST)
    public @ResponseBody HashMap postEditName(@ModelAttribute("user") @Valid User updated, BindingResult result){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
           User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.setFullName(updated.getFullName());
            userService.saveUser(user);
            response.put("status", "SUCCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("errors", result);
        }
        return response;
    }
}