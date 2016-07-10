/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.validators;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author Bert
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware{

    private static ApplicationContext CONTEXT;
    
    public static Object getBean(Class clazz){
       return ApplicationContextProvider.CONTEXT.getBean(clazz);
    }
    
    public static Object getBean(String qualifier, Class clazz){
        return ApplicationContextProvider.CONTEXT.getBean(qualifier,clazz);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        ApplicationContextProvider.CONTEXT = ac;
    }
    
}
