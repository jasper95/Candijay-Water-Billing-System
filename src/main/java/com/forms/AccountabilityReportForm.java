package com.forms;

import com.domain.Address;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by jasper on 8/12/16.
 */
public class AccountabilityReportForm {
    @NotNull(message="Please select a type")
    private Integer type;
    @NotEmpty(message="Please select a barangay") @NotNull(message="Please select a barangay")
    private String barangay;
    @NotNull(message="Please select a zone")
    private Integer zone;

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
}
