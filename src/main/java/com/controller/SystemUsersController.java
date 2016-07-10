/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.UserRepository;
import com.domain.Role;
import com.domain.User;
import com.service.SystemUserService;
import java.beans.PropertyEditorSupport;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Bert
 */
@RequestMapping("/admin/system-users")
@Controller
@SessionAttributes("user")
public class SystemUsersController {
    @Autowired
    private SystemUserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;
    static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.user";
    
    @InitBinder
    protected void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Role.class, new CustomRoleEditor());
    }
    
    private class CustomRoleEditor extends PropertyEditorSupport{
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(userService.findRoleById(Long.valueOf(text)));
        }      
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String getSystemUsers(){
        return "systemUsers/usersList";
    }
    
    @RequestMapping(value="/get-all", method=RequestMethod.GET)
    public @ResponseBody List<User> getAll(){
        return userService.getAllUsers();
    }
    
    @RequestMapping(value="/new")
    public String getNew(ModelMap model){
        model.addAttribute("roles", userService.getAllRoles());
        if(!model.containsAttribute(BINDING_RESULT_NAME)){
            model.addAttribute("user", new User());
        }
        model.addAttribute("createOrUpdate", "Create");
        return "systemUsers/createOrUpdateUserForm";
    }
    
    @RequestMapping(value="/update/{username}")
    public String getUpdate(@PathVariable("username") String username, ModelMap model){
        model.addAttribute("roles", userService.getAllRoles());
        if(!model.containsAttribute(BINDING_RESULT_NAME)){
            User user = userRepo.findByUsername(username);
            if(user == null){
                model.put("type", "Bad request URL");
                model.put("message", "Please avoid retrieving admin pages via URL");
                return "errors";
            }
            model.addAttribute("user", user);
        }
        model.addAttribute("createOrUpdate", "Update");
        return "systemUsers/createOrUpdateUserForm";
    }
    
    @RequestMapping(value="/new", method=RequestMethod.POST)
    public String postProcessUserForm(@ModelAttribute("user") @Valid User user, BindingResult result, RedirectAttributes redirectAttributes){
        
        if(userService.isUsernameAlreadyTaken(user.getUsername()))
            result.rejectValue("username", "", "Username already taken");
        
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:/admin/system-users/new";
        }
        else{
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
        }
        return "redirect:/admin/system-users";
    }
    
    @RequestMapping(value="/update/{username}", method=RequestMethod.POST)
    public String postUpdate(@ModelAttribute("user") @Valid User user, BindingResult result, @PathVariable("username") String username,
                    RedirectAttributes redirectAttributes){
        if(!result.hasErrors()){
            try{
               userService.saveUser(user);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("", "This record was modified by another user. Try refreshing the page.");
            }
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
            return "redirect:/admin/system-users/update/"+username;
        }
        return "redirect:/admin/system-users";
    }
}