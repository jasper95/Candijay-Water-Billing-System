/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.Address;
import com.domain.Invoice;
import com.domain.Schedule;

import java.math.BigDecimal;
import java.util.List;

import com.domain.enums.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Bert
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
    Invoice findTopByAccountOrderBySchedule_YearDescSchedule_MonthDesc(Account account);
    @EntityGraph(attributePaths = {"account", "reading"}, type= EntityGraph.EntityGraphType.LOAD)
    Invoice findById(Long id);
    @EntityGraph(attributePaths = {"account"}, type= EntityGraph.EntityGraphType.LOAD)
    List<Invoice> findByScheduleAndAccount_AddressAndStatusNotOrderByAccount_Customer_LastnameAscAccount_Customer_FirstNameAsc(Schedule sched, Address address, InvoiceStatus status);
    @EntityGraph(attributePaths = {"account"}, type= EntityGraph.EntityGraphType.LOAD)
    List<Invoice> findByScheduleAndStatusNotOrderByAccount_Customer_LastnameAscAccount_Customer_FirstNameAsc(Schedule sched, InvoiceStatus status);
    @Query(value = "SELECT SUM(i.remaining_total) FROM Invoice i WHERE i.schedule_id = ?1", nativeQuery = true)
    BigDecimal findTotalCollectiblesBySchedule(@Param("id") Long id);
    List<Invoice> findByAccountOrderByIdDesc(Account account, Pageable pageable);
    @EntityGraph(attributePaths = {"account", "reading"}, type= EntityGraph.EntityGraphType.LOAD)
    Invoice findTopByAccountAndSchedule(Account account, Schedule schedule);
}