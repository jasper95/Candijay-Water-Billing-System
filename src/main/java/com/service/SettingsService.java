/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Settings;

/**
 * interface created for settings module.
 * @author Bert
 */
public interface SettingsService {
    Settings getCurrentSettings();
    void updateSettings(Settings settings);
}
