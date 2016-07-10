/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface DeviceRepository extends JpaRepository<Device, Long>{
    Device findByOwnerAndActive(Account owner, boolean active);
    Device findByMeterCode(String meterCode);
    List<Device> findByOwner(Account owner);
}
