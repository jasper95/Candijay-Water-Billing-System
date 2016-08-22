/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charts;

import java.math.BigDecimal;

/**
 *
 * @author Bert
 */
public class ChartData {
    private BigDecimal value;
    private String month;
    private String category;

    public ChartData(BigDecimal value, String month, String category) {
        this.value = value;
        this.month = month;
        this.category = category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
