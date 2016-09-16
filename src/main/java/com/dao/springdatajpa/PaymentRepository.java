/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Payment;
import com.domain.Schedule;
import java.util.List;

import com.domain.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author 201244055
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment>  findByInvoice_ScheduleAndAccount_Address_BrgyAndInvoice_StatusNot(Schedule sched, String brgy, InvoiceStatus status);
    List<Payment> findByInvoice_ScheduleAndInvoice_StatusNot(Schedule sched, InvoiceStatus status);
    Payment findByReceiptNumber(String receiptNumber);
}
