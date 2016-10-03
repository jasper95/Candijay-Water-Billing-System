/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.RoleRepository;
import com.dao.springdatajpa.UserRepository;
import com.domain.Role;
import com.domain.User;
import com.domain.enums.UserStatus;
import com.service.SystemUserService;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Bert
 */
@RequestMapping("/admin/system-users")
@Controller
public class SystemUsersController {
    private SystemUserService userService;
    private UserRepository userRepo;
    private PasswordEncoder encoder;
    private RoleRepository roleRepo;

    @Autowired
    public SystemUsersController(SystemUserService userService, UserRepository userRepo, PasswordEncoder encoder,
                                 RoleRepository roleRepo){
        this.userService = userService;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
        binder.registerCustomEditor(Role.class, new CustomRoleEditor());
        binder.registerCustomEditor(Date.class,  new CustomDateEditor(dateFormat, true));
    }
    
    private class CustomRoleEditor extends PropertyEditorSupport{
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(roleRepo.findOne(Long.valueOf(text)));
        }      
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getSystemUsers(ModelMap model) {
        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("user", new User());
        return "systemUsers/usersList";
    }
    
    @RequestMapping(value="/get-all", method=RequestMethod.GET)
    public @ResponseBody List<User> getAll(){
        return userRepo.findAll();
    }

    
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public @ResponseBody
    HashMap processUserForm(@ModelAttribute("user") @Valid User user, BindingResult result){
        HashMap response = new HashMap();
        if(user.getRoles().isEmpty())
            result.reject("global", "Please select at least one role.");
        if(!result.hasErrors())
            if(userRepo.findByUsername(user.getUsername()) != null)
                result.rejectValue("username", "", "Username already taken");
        if(!result.hasErrors()){
            user.setPassword(encoder.encode(user.getPassword()));
            response.put("user",userService.saveUser(user));
            response.put("status", "SUCCESS");
        }
        else{
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
        }
        return response;

    }
    
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public @ResponseBody HashMap postUpdate(@ModelAttribute("user") User userForm, BindingResult result, @RequestParam Map<String, String> params){
        HashMap response = new HashMap();
        int status = Integer.valueOf(params.get("updateStatus"));
        User user = userRepo.findOne(userForm.getId());
        if(userForm.getRoles().isEmpty())
            result.reject("global", "Please select at least one role.");
        if(!result.hasErrors()) {
            if (user != null) {
                user.setRoles(userForm.getRoles());
                if (status == 1)
                    user.setStatus(UserStatus.ACTIVE);
                else
                    user.setStatus(UserStatus.INACTIVE);
                user.setVersion(userForm.getVersion());
            } else result.reject("global", "User does not exists");
        }
        if(!result.hasErrors()){
            try{
               response.put("user", userService.saveUser(user));
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("global", "This record was modified by another user. Try closing the form and edit the user again.");
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

    @RequestMapping(value="/find/{id}", method=RequestMethod.GET)
    public @ResponseBody HashMap findUser(@PathVariable("id") Long id){
        HashMap response = new HashMap();
        if(id != null){
            User user = userRepo.findById(id);
            if( user != null){
                response.put("user", user);
                response.put("status", "SUCCESS");
                return response;
            }
        }
        response.put("status", "FAILURE");
        return response;
    }
}