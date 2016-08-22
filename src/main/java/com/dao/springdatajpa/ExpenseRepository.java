package com.dao.springdatajpa;

import com.domain.Expense;
import com.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jasper on 7/26/16.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Expense findByScheduleAndType(Schedule sched, Integer type);
    List<Expense> findBySchedule(Schedule sched);
}
