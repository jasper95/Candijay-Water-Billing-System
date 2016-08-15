package com.dao.springdatajpa;

import com.domain.ModifiedPayment;
import com.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jasper on 8/3/16.
 */
public interface ModifiedPaymentRepository extends JpaRepository<ModifiedPayment, Long> {

}
