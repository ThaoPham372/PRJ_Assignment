
package com.gym.service;

import com.gym.dao.AdminDAO;
import com.gym.dao.UserDAO;
import com.gym.model.Admin;
import com.gym.model.User;
import java.util.List;

/*
    Note: 
 */
public class AdminService implements IAdminService{
    
    //Admin
    @Override
    public List<Admin> getAdmins() {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.findAll();
    }
    
    @Override
    public Admin getAdminById(long id) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.findById(id);
    }
    
    @Override
    public Admin getAdminByName(String name) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.findByName(name);
    }
    
    @Override
    public Admin getAdminByEmail(String email) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.findByEmail(email);
    }
    
    @Override
    public boolean addAdmin(Admin admin) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.add(admin);
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.update(admin);
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        AdminDAO adminDAO = new AdminDAO();
        return adminDAO.delete(admin);
    }
    
    //User
    @Override
    public List<User> getUsers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean addUser(User user) {
        UserDAO userDAO = new UserDAO();
        userDAO.insertUser(user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getSalt());
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean changeUserPassword(User user, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
