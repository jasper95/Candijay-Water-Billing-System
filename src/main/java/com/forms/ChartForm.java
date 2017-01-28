/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Bert
 */
public class ChartForm {
    @NotNull(message="This field is required")
    private Integer year;
    @NotNull(message="This field is required")
    private Integer type;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
}
