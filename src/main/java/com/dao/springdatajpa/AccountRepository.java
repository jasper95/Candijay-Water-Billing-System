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
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Bert
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
    List<Account> getByCustomer_Id(Long id);
    Account findByNumber(String number);
    Long countByStatus(AccountStatus status);
    Long countByAddressIn(Collection<Address> address);
    Long countByAddressInAndStatusUpdated(Collection<Address> address, boolean statusUpdated);
    Long countByAddressInAndStatus(Collection<Address> addresses, AccountStatus status);
    List<Account> findByAddressInAndStatusIn(Collection<Address> address, Collection<AccountStatus> statuses);
    List<Account> findByAddressInAndStatusUpdatedAndStatusIn(Collection<Address> addresses, boolean statusUpdated, Collection<AccountStatus> statuses);
}
