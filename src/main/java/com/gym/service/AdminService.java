package com.gym.service;

import com.gym.dao.AdminDAO;
import com.gym.dao.UserDAO;
import com.gym.model.Admin;
import com.gym.model.User;
import java.util.List;

/*
    Note: 
 */
public class AdminService {
    
    private final AdminDAO adminDAO;
    private final UserDAO userDAO;
    
    public AdminService() {
        adminDAO = new AdminDAO();
        userDAO = new UserDAO();
    }
    
    //Admin
    public List<Admin> getAll() {
        return adminDAO.findAll();
    }
    
    public Admin getAdminById(int id) {
        return adminDAO.findById(id);
    }
    
    public Admin getAdminByName(String name) {
        return adminDAO.findByName(name);
    }
    
    public Admin getAdminByEmail(String email) {
        return adminDAO.findByEmail(email);
    }
    
    public int add(Admin admin) {
        return adminDAO.save(admin);
    }

    public int update(Admin admin) {
        return adminDAO.update(admin);
    }

    public int delete(Admin admin) {
        return adminDAO.delete(admin);
    }
    
    //User
    public List<User> getUsers() {
        return userDAO.findAll();
    }

    public boolean addUser(User user) {
        return userDAO.save(user) > -1;
    }

    public boolean updateUser(User user) {
        return userDAO.update(user) > -1;
    }

    public boolean deleteUser(User user) {
        return userDAO.delete(user) > -1;
    }

    public boolean changeUserPassword(User user, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
