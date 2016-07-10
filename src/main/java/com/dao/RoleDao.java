/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.domain.Role;
import java.util.List;

/**
 *
 * @author Bert
 */
public interface RoleDao {
    public void addRole(Role role);
    public void updateRole(Role role);
    public <T> List<T> getAllRoles();
    public void removeRole(Role role);
    public Role findRoleById(int id);
}
