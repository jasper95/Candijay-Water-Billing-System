/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.domain.Settings;
import com.service.SettingsService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Bert
 */
@RequestMapping("/admin/settings")
@Controller
@SessionAttributes("settings")
public class SettingsController {
    
    @Autowired
    private SettingsService settingsService;
    private static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult.settings";
    
    @RequestMapping(method=RequestMethod.GET)
    public String getUpdateSettings(ModelMap model){
        if(!model.containsAttribute(BINDING_RESULT_NAME))
            model.addAttribute("settings", settingsService.getCurrentSettings());
        return "settings/updateSettings";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String postUpdateSettings(@ModelAttribute("settings") @Valid Settings settings, BindingResult result, RedirectAttributes redirectAttributes){
        if(!result.hasErrors()){
            try{
                settingsService.updateSettings(settings);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("", "This record was modified by another user. Try refreshing the page.");
            }catch(Exception e){
                result.reject("UnexpectedError");
            }
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, result);
        }
        else redirectAttributes.addFlashAttribute("updateSuccess", 1);
        return "redirect:/admin/settings/";
    }
}