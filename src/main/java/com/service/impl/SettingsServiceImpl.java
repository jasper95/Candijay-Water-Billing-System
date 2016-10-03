/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.SettingsRepository;
import com.domain.Settings;
import com.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Bert
 */
@Service("settingsService")
public class SettingsServiceImpl implements SettingsService {

    private SettingsRepository settingsRepo;

    @Autowired
    public SettingsServiceImpl(SettingsRepository settingsRepo){
        this.settingsRepo = settingsRepo;
    }
    
    @Transactional(readOnly=true)
    @Override
    public Settings getCurrentSettings() {
        return settingsRepo.findAll().get(0);
    }
    
    @Transactional
    @Override
    public void updateSettings(Settings settings) {
        settingsRepo.save(settings);
    }  
}
