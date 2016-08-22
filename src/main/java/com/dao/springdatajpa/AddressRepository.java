/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bert
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByBrgyAndLocationCode(String brgy, Integer locationCode);
}