/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forms;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Bert
 */
public class ReportForm {
    private String barangay;
    @NotNull(message="Please select a month")
    private Integer month;
    @NotNull(message="Please select a year")
    private Integer year;
    @NotNull
    private Integer summary;
    @NotNull(message="Please select a type") @NotEmpty(message="Please select a type")
    private String type;
    public String getBarangay() {
        return barangay;
    }
    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSummary() {
        return summary;
    }

    public void setSummary(Integer summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
