package com.dao.springdatajpa;

import com.domain.ModifiedExpense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jasper on 8/13/16.
 */
public interface ModifiedExpenseRepository extends JpaRepository<ModifiedExpense, Long> {
}
