package com.dao.util;


import com.domain.Payment;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;

/**
 * Created by jasper on 8/2/16.
 */
public class CustomAuditingEntityListener {
    @PostUpdate
    public void touchForUpdate(Payment payment){
        System.out.println(payment.getAmountPaid());
        System.out.println("post-update");
    }
    @PostLoad
    public void afterLoad(Payment payment){
        System.out.println(payment.getAmountPaid());
        System.out.println("data loaded");
    }
}
