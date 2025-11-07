
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

    // User
    public List<User> getAll() {
        List<User> users = userDAO.findAll();
        sortById(users);
        return users;
    }

    private void sortById(List<User> users) {
        users.sort((u1, u2) -> Integer.compare(u1.getId(), u2.getId()));
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public User getUserByUserame(String username) {
        return userDAO.findByUsername(username);
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
