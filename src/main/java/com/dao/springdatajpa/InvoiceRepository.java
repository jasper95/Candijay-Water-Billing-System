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
    Invoice findByAccountAndSchedule(Account account, Schedule schedule);
    Invoice findTopByAccountOrderByIdDesc(Account account);
    @EntityGraph(attributePaths = {"account", "reading"}, type= EntityGraph.EntityGraphType.LOAD)
    Invoice findTopByAccount_IdOrderByIdDesc(Long id);
    @EntityGraph(attributePaths = {"account", "reading"}, type= EntityGraph.EntityGraphType.LOAD)
    Invoice findById(Long id);
    @EntityGraph(attributePaths = {"account"}, type= EntityGraph.EntityGraphType.LOAD)
    List<Invoice> findByScheduleAndAccount_Address(Schedule sched, Address address);
    @EntityGraph(attributePaths = {"account"}, type= EntityGraph.EntityGraphType.LOAD)
    List<Invoice> findBySchedule(Schedule sched);
    @Query(value = "SELECT SUM(i.net_charge) FROM Invoice i WHERE i.schedule_id = ?1", nativeQuery = true)
    BigDecimal findTotalCollectiblesBySchedule(@Param("id") Long id);
    List<Invoice> findByAccountOrderByIdDesc(Account account, Pageable pageable);
}