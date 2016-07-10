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
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
    public Invoice findByAccountAndSchedule(Account account, Schedule schedule);
    public Invoice findTopByAccountOrderByIdDesc(Account account);
    public List<Invoice> findByScheduleAndAccount_Address_Brgy(Schedule sched, String brgy);
    public List<Invoice> findBySchedule(Schedule sched);
}