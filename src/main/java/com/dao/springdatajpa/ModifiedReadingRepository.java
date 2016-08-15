package com.dao.springdatajpa;

import com.domain.ModifiedReading;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jasper on 8/14/16.
 */
public interface ModifiedReadingRepository extends JpaRepository<ModifiedReading, Long> {
}
