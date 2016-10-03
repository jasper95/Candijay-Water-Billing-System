/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author Bert
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByOrderByBrgyAsc();
    Address findByBrgy(String brgy);
    List<Address> findByLocationCode(Integer locationCode);
}