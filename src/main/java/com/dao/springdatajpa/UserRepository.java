/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.springdatajpa;

import com.domain.User;
import com.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author Bert
 */
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    List<User> findByStatus(UserStatus status);
}
