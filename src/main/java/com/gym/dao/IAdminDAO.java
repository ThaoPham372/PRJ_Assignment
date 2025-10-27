
package com.gym.dao;

import com.gym.model.Admin;
import java.util.List;

/*
    Note: 
 */
public interface IAdminDAO {
    List<Admin> findAll();
    Admin findById(long id);
    Admin findByName(String name);
    Admin findByEmail(String email);
    
    boolean add(Admin admin);
    boolean update(Admin admin);
    boolean delete(Admin admin);
    
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    Admin findByNameOrEmail(String nameOrEmail);
    boolean incrementFailedLoginAttempts(Admin admin);
    boolean resetFailedLoginAttempts(Admin admin);
    boolean resetLockedUntil(Admin admin);
    boolean lockAccount(Admin admin, int minutes);
    boolean updateLastLogin(long adminId);
}
