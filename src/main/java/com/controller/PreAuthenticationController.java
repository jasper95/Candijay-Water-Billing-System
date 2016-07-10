/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.springdatajpa.UserRepository;
import com.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 * @author Bert
 */
@Controller
public class PreAuthenticationController {

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken))
            return "redirect:/admin";
        
        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        return "login";
    }
    
    @RequestMapping(value="/about", method=RequestMethod.GET)
    public String about(){
        return "about";
    }
    
    @RequestMapping(value="/faq", method=RequestMethod.GET)
    public String faq(){
        return "faq";
    }
    
    @RequestMapping(value="/contact", method=RequestMethod.GET)
    public String contact(){
        return "contact";
    }
    
    @RequestMapping(value="/staff", method=RequestMethod.GET)
    public String staff(){
        return "staff";
    }

    @RequestMapping(value="/test", method=RequestMethod.GET)
    public @ResponseBody List<User> test(){
        return userRepo.findAll();
    }
}
