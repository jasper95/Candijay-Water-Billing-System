/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.Address;
import com.domain.Customer;

import java.util.Collection;
import java.util.List;

import com.domain.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author Bert
 */
public interface AccountRepository extends JpaRepository<Account,Long>{
    List<Account> findByCustomer(Customer customer);
    Account findByNumber(String number);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByAddressAndStatusUpdatedAndStatus(Address address, boolean statusUpdated, AccountStatus status);
    List<Account> findByAddressAndStatus(Address address, AccountStatus status);
    List<Account> findByAddressAndStatusUpdatedAndStatusIn(Address address, boolean statusUpdated, Collection<AccountStatus> statuses);
}
