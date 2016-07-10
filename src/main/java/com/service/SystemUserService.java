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
    public List<User> getAllUsers();
    public List<Role> getAllRoles();
    public void saveUser(User user);
    public void updateUser(User user);
    public boolean isUsernameAlreadyTaken(String username);
    public Role findRoleById(Long id);
}
