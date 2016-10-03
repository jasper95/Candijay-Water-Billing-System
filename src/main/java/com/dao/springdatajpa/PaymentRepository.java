/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Address;
import com.domain.Payment;
import com.domain.Schedule;

import java.math.BigDecimal;
import java.util.List;

import com.domain.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author 201244055
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @EntityGraph(attributePaths = {"account", "invoice"}, type= EntityGraph.EntityGraphType.LOAD)
    Payment findById(Long id);
    @Query("SELECT p FROM Payment p " +
            "JOIN FETCH p.account a " +
            "JOIN FETCH a.address ad " +
            "JOIN FETCH p.invoice i " +
            "JOIN FETCH i.schedule s " +
            "WHERE i.status <> :status AND s.id = :schedule AND ad.id = :address")
    List<Payment>  findByInvoice_ScheduleAndAccount_AddressAndInvoice_StatusNot(
            @Param("schedule") Long schedId,
            @Param("address") Long addressId,
            @Param("status") InvoiceStatus status);
    @Query("SELECT p FROM Payment p " +
            "JOIN FETCH p.account a " +
            "JOIN FETCH p.invoice i " +
            "JOIN FETCH i.schedule s " +
            "WHERE i.status <> :status AND s.id = :schedule")
    List<Payment> findByInvoice_ScheduleAndInvoice_StatusNot(
            @Param("schedule")Long schedId,
            @Param("status")InvoiceStatus status);
    Payment findByReceiptNumber(String receiptNumber);
    @Query(value="SELECT SUM(p.amount_paid) FROM Payment p, Invoice i WHERE i.id = p.invoice_id AND i.schedule_id = ?1", nativeQuery = true)
    BigDecimal findTotalCollectionBySchedule(Long id);
}