package com.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Bert on 10/7/2016.
 */
public class BillDiscountForm {
    @NotNull
    Long billId;
    @Digits(fraction=2, integer=10, message = "Invalid amount") @NotNull(message="This field is required")
    BigDecimal discount;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
