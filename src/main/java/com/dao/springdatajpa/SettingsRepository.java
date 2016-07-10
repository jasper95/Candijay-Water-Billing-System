/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    
}
