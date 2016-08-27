package com.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Bert on 8/26/2016.
 */
@ControllerAdvice
public class ControllerExceptionHandlers {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String methodNotSupported(Model model){
        if(SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return "redirect:/login";
        else {
            model.addAttribute("type","Request not allowed");
            model.addAttribute("message", "You don't have enough permission to fulfill the request.");
            return "errors";
        }
    }
}
