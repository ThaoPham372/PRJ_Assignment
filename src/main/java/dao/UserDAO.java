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
        return user.getUserId();
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

    public int existsByUsername(String name) {
        System.out.println(">>User: Exist By Username");
        User user = null;
        try {
            user = genericDAO.findByField("name", name);
        } catch (Exception e) {
            System.out.println("Result: " + user);
        }

        System.out.println("-------------------------------");
        return user != null ? user.getUserId() : -1;
    }

    public int existsByEmail(String email) {
        User user = genericDAO.findByField("email", email);
        return user != null ? user.getUserId() : -1;
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

    public int incrementFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        genericDAO.update(user);
        return user.getFailedLoginAttempts();
    }

    public int resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        genericDAO.update(user);
        return 0;
    }

    public int resetLockedUntil(User user) {
        user.setLockedUntil(null);
        genericDAO.update(user);
        return 0;
    }

    public int lockAccount(User user, int minutes) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MINUTE, minutes);
        user.setLockedUntil(cal.getTime());
        genericDAO.update(user);
        return minutes;
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
