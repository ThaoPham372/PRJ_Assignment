
package service;

import dao.UserDAO;
import model.User;
import java.util.List;

/*
    Note: 
 */
public class UserService {
    
    private final UserDAO userDAO;
    
    public UserService() {
        userDAO = new UserDAO();
    }
    
    //User
    public List<User> getAll() {
        return userDAO.findAll();
    }
    
    public User getUserById(int id) {
        return userDAO.findById(id);
    }
    
    public User getUserByName(String name) {
        return userDAO.findByName(name);
    }
    
    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    
    public int add(User user) {
        return userDAO.save(user);
    }

    public int update(User user) {
        return userDAO.update(user);
    }

    public int delete(User user) {
        return userDAO.delete(user);
    }
}
