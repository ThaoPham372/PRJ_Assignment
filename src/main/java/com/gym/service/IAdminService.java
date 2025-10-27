
package com.gym.service;

import com.gym.model.Admin;
import com.gym.model.User;
import java.util.List;

/*
    Note: 
    + Trainer
    + Product
    + Staff
    + ......
    
 */
public interface IAdminService {
    //Admin
    List<Admin> getAdmins();
    Admin getAdminById(long id);
    Admin getAdminByName(String name);
    Admin getAdminByEmail(String email);
    
    boolean addAdmin(Admin admin);
    boolean updateAdmin(Admin admin);
    boolean deleteAdmin(Admin admin);
    
    //User
    List<User> getUsers();
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(User user);
    boolean changeUserPassword(User user, String newPassword);
}
