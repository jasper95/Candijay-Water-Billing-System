/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Payment;
import com.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author 201244055
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoice_ScheduleAndAccount_Address_Brgy(Schedule sched, String brgy);
    List<Payment> findByInvoice_Schedule(Schedule sched);
    Payment findByReceiptNumber(String receiptNumber);
}
