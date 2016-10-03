/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.MeterReading;
import com.domain.Schedule;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Bert
 */
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findTop3ByAccountOrderByIdDesc(Account account);
    MeterReading findByAccountAndSchedule(Account account, Schedule schedule);
    @Query("SELECT m FROM MeterReading m JOIN FETCH m.account WHERE m.id = (:id)")
    MeterReading findByIdWithAccount(@Param("id") Long id);
    @Query(value="SELECT SUM(m.consumption) FROM METER_READING m WHERE m.schedule_id = ?1", nativeQuery = true)
    BigInteger findTotalConsumptionBySchedule(Long id);
}