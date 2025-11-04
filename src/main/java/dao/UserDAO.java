package dao;

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
        System.out.println(">>User: FIND ALL");
        System.out.println("result: ");
        List<User> users = genericDAO.findAll();
        if (users != null) {
            System.out.println("");
            for (User a : users) {
                System.out.println(a);
            }
        }
        System.out.println("-----------------------------");
        return users != null ? users : List.of();
    }

    public User findById(int id) {
        System.out.println(">>User: Find by ID");
        User user = genericDAO.findById(id);
        System.out.println("result: " + user);
        System.out.println("-------------------------------");
        return user;
    }

    public User findByName(String name) {
        System.out.println(">>User: Find By Name");
        User user = genericDAO.findByField("name", name);
        System.out.println("result: " + user);
        System.out.println("---------------------------");
        return user;
    }

    public User findByEmail(String email) {
        System.out.println(">>User: Find By Email");
        User user = genericDAO.findByField("email", email);
        System.out.println("result: " + user);
        System.out.println("---------------------------");
        return user;
    }

    public int existsByUsername(String name) {
        System.out.println(">>User: Exist By Username");
        User user = null;
        try{
            user = genericDAO.findByField("name", name);
        } catch(Exception e) {
            System.out.println("Result: " + user);
        }
            
        System.out.println("-------------------------------");
        return user != null ? user.getUserId() : -1;
    }

    public int existsByEmail(String email) {
        System.out.println(">>User: Exist By Email");
        User user = genericDAO.findByField("email", email);
        System.out.println("Result: " + user);
        System.out.println("-------------------------------");
        return user != null ? user.getUserId() : -1;
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        System.out.println(">>User: Find By Name Or Email");
        User user = null;
        try {
            user = genericDAO.findByField("username", usernameOrEmail);
            if (user == null) {
                user = genericDAO.findByField("email", usernameOrEmail);
            }
        } catch (Exception e) {
            System.out.println("Khong tim thay");
        }
        System.out.println("Result: " + user);
        System.out.println("---------------------------");
        return user;
    }

    public int update(User user) {
        System.out.println(">>User: Update");
        int id = genericDAO.update(user);
        System.out.println("Result id: " + id);
        System.out.println("---------------------------");
        return id;
    }

    public int incrementFailedLoginAttempts(User user) {
        System.out.println(">>User: Increment Failed Login Attempts");
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        genericDAO.update(user);
        System.out.println("New Failed Attempts: " + user.getFailedLoginAttempts());
        System.out.println("---------------------------");
        return user.getFailedLoginAttempts();
    }

    public int resetFailedLoginAttempts(User user) {
        System.out.println(">>User: Reset Failed Login Attempts");
        user.setFailedLoginAttempts(0);
        genericDAO.update(user);
        System.out.println("Failed Attempts reset to 0");
        System.out.println("---------------------------");
        return 0;
    }

    public int resetLockedUntil(User user) {
        System.out.println(">>User: Reset Locked Until");
        user.setLockedUntil(null);
        genericDAO.update(user);
        System.out.println("LockedUntil reset to null");
        System.out.println("---------------------------");
        return 0;
    }

    public int lockAccount(User user, int minutes) {
        System.out.println(">>User: Lock Account for " + minutes + " minutes");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MINUTE, minutes);
        user.setLockedUntil(cal.getTime());
        genericDAO.update(user);
        System.out.println("Account locked until: " + user.getLockedUntil());
        System.out.println("---------------------------");
        return minutes;
    }

    public void updateLastLogin(int userId) {
        System.out.println(">>User: Update Last Login");
        User user = genericDAO.findById(userId);
        if (user != null) {
            user.setLastLogin(new java.util.Date());
            genericDAO.update(user);
            System.out.println("Last login updated to: " + user.getLastLogin());
        }
        System.out.println("---------------------------");
    }

    public int delete(User user) {
        System.out.println(">>User: Delete (update status)");
        user.setStatus("INACTIVE");
        int userId = genericDAO.update(user);
        System.out.println("update user id: " + userId + ", status: " + user.getStatus());
        System.out.println("---------------------------");
        return userId;
    }

}
