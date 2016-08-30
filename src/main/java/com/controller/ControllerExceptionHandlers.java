package com.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerExceptionHandlers {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String methodNotSupported(Model model){
        if(SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return "login";
        else {
            model.addAttribute("type","Request not allowed");
            model.addAttribute("message", "Sorry, your request could not be processed. Please try again.");
            return "errors";
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(Model model, Authentication auth){
        if(auth == null ||  !auth.isAuthenticated())
            return "error404";
        else {
            model.addAttribute("type", "Requested page not found");
            model.addAttribute("message","The content does not exists or might have been deleted");
            return "errors";
        }
    }
}
