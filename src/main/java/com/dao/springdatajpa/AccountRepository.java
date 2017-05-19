/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.Account;
import com.domain.Address;

import java.util.Collection;
import java.util.List;

import com.domain.enums.AccountStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Bert
 */
public interface AccountRepository extends JpaRepository<Account,Long>{
    List<Account> getByCustomer_Id(Long id);
    Account findByNumber(String number);
    Long countByStatus(AccountStatus status);
    Long countByAddress(Address address);
    Long countByAddressAndStatusUpdated(Address address, boolean statusUpdated);
    Long countByAddressInAndStatus(Collection<Address> addresses, AccountStatus status);
    Long countByAddressAndPurokIn(Address address, Collection<Integer> puroks);
    List<Account> findByAddressInAndStatusIn(Collection<Address> address, Collection<AccountStatus> statuses);
    List<Account> findByAddressAndStatusInOrderByPurokAsc(Address address, Collection<AccountStatus> statuses);
    List<Account> findByAddressAndStatusUpdatedAndStatusInOrderByPurokAsc(Address address, boolean statusUpdated, Collection<AccountStatus> statuses);
    List<Account> findByAddressAndPurokInAndStatusUpdatedAndStatusIn(Address address, Collection<Integer> puroks, boolean statusUpdated, Collection<AccountStatus> statuses);
    List<Account> findByAddressAndPurokInAndStatusIn(Address address, Collection<Integer> puroks, Collection<AccountStatus> statuses);
}
