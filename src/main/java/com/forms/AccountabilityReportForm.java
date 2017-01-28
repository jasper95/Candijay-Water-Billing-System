package com.forms;

import javax.validation.constraints.NotNull;

/**
 * Created by jasper on 8/12/16.
 */
public class AccountabilityReportForm {
    @NotNull(message="Please select a type")
    private Integer type;
    private String barangay;
    private Integer zone;
    private Integer printBrgy;

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
}
