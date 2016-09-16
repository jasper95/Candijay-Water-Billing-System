/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.Invoice;
import com.domain.Schedule;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
    Invoice findByAccountAndSchedule(Account account, Schedule schedule);
    Invoice findTopByAccountOrderByIdDesc(Account account);
    List<Invoice> findByScheduleAndAccount_Address_Brgy(Schedule sched, String brgy);
    List<Invoice> findBySchedule(Schedule sched);
    List<Invoice> findByAccountOrderByIdDesc(Account account, Pageable pageable);
}