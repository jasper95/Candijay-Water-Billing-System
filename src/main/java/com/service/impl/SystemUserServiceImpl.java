/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.springdatajpa.RoleRepository;
import com.dao.springdatajpa.UserRepository;
import com.domain.Role;
import com.domain.User;
import com.domain.enums.UserType;
import com.service.SystemUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Bert
 */
@Service("userService")
public class SystemUserServiceImpl implements SystemUserService {


    private UserRepository userRepo;

    @Autowired
    public SystemUserServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        if(user.getRoles().size() < 5)
            user.setType(UserType.LIMITED);
        else user.setType(UserType.SUPERUSER);
        return userRepo.save(user);
    }
}
