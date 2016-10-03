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
    @EntityGraph(attributePaths = {"address", "customer"})
    List<Account> getByCustomer(Customer customer);
    List<Account> getByCustomer_Id(Long id);
    Account findByNumber(String number);
    Long countByStatus(AccountStatus status);
    Long countByAddressIn(Collection<Address> address);
    Long countByAddressInAndStatusUpdated(Collection<Address> address, boolean statusUpdated);
    List<Account> findByAddressAndStatus(Address address, AccountStatus status);
    List<Account> findByAddressIn(Collection<Address> addresses);
    List<Account> findByAddressInAndStatusUpdatedAndStatusIn(Collection<Address> addresses, boolean statusUpdated, Collection<AccountStatus> statuses);
}
