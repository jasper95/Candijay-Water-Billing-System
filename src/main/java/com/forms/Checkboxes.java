/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author 201244055
 */
public class Checkboxes {
    @NotNull
    private List<Long> checkboxValues;

    public Checkboxes() {
    }
    
    public List<Long> getCheckboxValues() {
        return checkboxValues;
    }

    public void setCheckboxValues(List<Long> checkboxValues) {
        this.checkboxValues = checkboxValues;
    }
       
}
