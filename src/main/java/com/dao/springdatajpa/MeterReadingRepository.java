/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.MeterReading;
import com.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findTop3ByAccountOrderByIdDesc(Account account);
    MeterReading findByAccountAndSchedule(Account account, Schedule schedule);
    List<MeterReading> findBySchedule(Schedule sched);
}