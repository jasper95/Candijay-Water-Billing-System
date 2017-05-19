package com.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jasper on 8/12/16.
 */
public class AccountabilityReportForm {
    @NotNull(message="This field is required")
    private Integer type;
    @NotEmpty(message = "This field is required")
    private String barangay;
    private Integer zone;
    private Integer printBrgy;
    private List<Integer> puroks;
    private Integer month;
    private Integer year;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public Integer getZone() {
        return zone;
    }

    public void setZone(Integer zone) {
        this.zone = zone;
    }

    public Integer getPrintBrgy() {
        return printBrgy;
    }

    public void setPrintBrgy(Integer printBrgy) {
        this.printBrgy = printBrgy;
    }

    public List<Integer> getPuroks() {
        return puroks;
    }

    public void setPuroks(List<Integer> puroks) {
        this.puroks = puroks;
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
}
