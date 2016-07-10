/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Bert
 */
@Entity
@Table(name="address_group"
    ,catalog="revised_cws_db"
)
public class AddressGroup implements java.io.Serializable {
    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @Column(name="account_prefix", length=2)
    private String accountPrefix;
    @Column(name="accounts_count", nullable=false)
    private Integer accountsCount;
    @Column(name="due_day", nullable=false)
    private Integer dueDay;

    public AddressGroup() {
    }

    public AddressGroup(String accountPrefix, Integer accountsCount, Integer dueDay) {
        this.accountPrefix = accountPrefix;
        this.accountsCount = accountsCount;
        this.dueDay = dueDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountPrefix() {
        return accountPrefix;
    }

    public void setAccountPrefix(String accountPrefix) {
        this.accountPrefix = accountPrefix;
    }

    public Integer getAccountsCount() {
        return accountsCount;
    }

    public void setAccountsCount(Integer accountsCount) {
        this.accountsCount = accountsCount;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }
     
}
