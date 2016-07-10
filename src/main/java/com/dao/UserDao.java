/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.domain.User;
import java.util.List;

/**
 *
 * @author Bert
 */
public interface UserDao {
    public void addUser(User user);
    public void updateUser(User user);
    public <T> List<T> listUsers(Class<T> clazz);
    public User getUserByUsername(String username);
    public void removeUser(String username);
    public User getUserByAttribute(Object attribute, String fieldName) throws Exception;
}
