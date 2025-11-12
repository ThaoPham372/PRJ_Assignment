package dao;

import java.util.ArrayList;
import java.util.List;
import model.User;

/*
    Note: 
 */
public class UserDAO {

    GenericDAO<User> genericDAO;

    public UserDAO() {
        genericDAO = new GenericDAO<>(User.class);
    }

    public int save(User user) {
        genericDAO.save(user);
        return user.getId();
    }

    public List<User> findAll() {
        List<User> users = genericDAO.findAll();
        return users != null ? users : new ArrayList<>(List.of());
    }

    public User findById(int id) {
        System.out.println(">>User: Find by ID");
        User user;
        try {
            user = genericDAO.findById(id);
            System.out.println("result: " + user);
        } catch (Exception e) {
            System.out.println("UserDAO -> Error find by id: " + e.getMessage());
            return null;
        }
        System.out.println("------------------------------- FOUND");
        return user;
    }

    public User findByUsername(String username) {
        User user;
        try {
            user = genericDAO.findByField("username", username);
        } catch (Exception e) {
            System.out.println("UserDAO -> Error find by user name: " + e.getMessage());
            return null;
        }
        return user;
    }

    public User findByEmail(String email) {
        System.out.println(">>User: Find By Email");
        User user;
        try {
            user = genericDAO.findByField("email", email);
            System.out.println("result: " + user);
        } catch (Exception e) {
            System.out.println("UserDAO -> Error find by email: " + e.getMessage());
            return null;
        }
        System.out.println("--------------------------- FOUND");
        return user;
    }

      public boolean existsByUsername(String username) {
        try {
            User user = genericDAO.findByField("username", username);
            return user != null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking username existence: " + e.getMessage());
        return false;
        }
    }

    public int existsByEmail(String email) {
        User user = genericDAO.findByField("email", email);
        return user != null ? user.getId() : -1;
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        User user = null;
        try {
            user = genericDAO.findByField("username", usernameOrEmail);
            if (user == null) {
                user = genericDAO.findByField("email", usernameOrEmail);
            }
        } catch (Exception e) {
            System.out.println("Khong tim thay");
        }
        return user;
    }

    public int update(User user) {
        int id = genericDAO.update(user);
        return id;
    }

    public void updateLastLogin(int userId) {
        User user = genericDAO.findById(userId);
        if (user != null) {
            user.setLastLogin(new java.util.Date());
            genericDAO.update(user);
        }
    }

    public int delete(User user) {
        user.setStatus("INACTIVE");
        int userId = genericDAO.update(user);
        return userId;
    }

}
