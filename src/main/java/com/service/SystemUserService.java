/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.Role;
import com.domain.User;
import java.util.List;

/**
 *
 * @author Bert
 */
public interface SystemUserService {
    User saveUser(User user);
}
