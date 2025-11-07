package dao;

import java.util.List;
import model.Admin;

/*
    Note: 
 */
public class AdminDAO {

    GenericDAO<Admin> genericDAO;

    public AdminDAO() {
        genericDAO = new GenericDAO<>(Admin.class);
    }

    public int save(Admin admin) {
        genericDAO.save(admin);
        return admin.getId();
    }

    public List<Admin> findAll() {
        System.out.println(">>Admin: FIND ALL");
        System.out.println("result: ");
        List<Admin> admins = genericDAO.findAll();
        if (admins != null) {
            System.out.println("");
            for (Admin a : admins) {
                System.out.println(a);
            }
        }
        System.out.println("-----------------------------");
        return admins != null ? admins : List.of();
    }

    public Admin findById(int id) {
        System.out.println(">>Admin: Find by ID");
        Admin admin = genericDAO.findById(id);
        System.out.println("result: " + admin);
        System.out.println("-------------------------------");
        return admin;
    }

    public Admin findByName(String name) {
        System.out.println(">>Admin: Find By Name");
        Admin admin = genericDAO.findByField("name", name);
        System.out.println("result: " + admin);
        System.out.println("---------------------------");
        return admin;
    }

    public Admin findByEmail(String email) {
        System.out.println(">>Admin: Find By Email");
        Admin admin = genericDAO.findByField("email", email);
        System.out.println("result: " + admin);
        System.out.println("---------------------------");
        return admin;
    }

    public int existsByName(String name) {
        System.out.println(">>Admin: Exist By Name");
        Admin admin = genericDAO.findByField("name", name);
        System.out.println("Result: " + admin);
        System.out.println("-------------------------------");
        return admin != null ? admin.getId() : -1;
    }

    public int existsByEmail(String email) {
        System.out.println(">>Admin: Exist By Email");
        Admin admin = genericDAO.findByField("email", email);
        System.out.println("Result: " + admin);
        System.out.println("-------------------------------");
        return admin != null ? admin.getId() : -1;
    }

    public Admin findByNameOrEmail(String nameOrEmail) {
        System.out.println(">>Admin: Find By Name Or Email");
        Admin admin = genericDAO.findByField("name", nameOrEmail);
        if (admin == null) {
            admin = genericDAO.findByField("email", nameOrEmail);
        }
        System.out.println("Result: " + admin);
        System.out.println("---------------------------");
        return admin;
    }

    public int update(Admin admin) {
        System.out.println(">>Admin: Update");
        int id = genericDAO.update(admin);
        System.out.println("Result id: " + id);
        System.out.println("---------------------------");
        return id;
    }

    public void updateLastLogin(int adminId) {
        System.out.println(">>Admin: Update Last Login");
        Admin admin = genericDAO.findById(adminId);
        if (admin != null) {
            admin.setLastLogin(new java.util.Date());
            genericDAO.update(admin);
            System.out.println("Last login updated to: " + admin.getLastLogin());
        }
        System.out.println("---------------------------");
    }

    public int delete(Admin admin) {
        System.out.println(">>Admin: Delete (update status)");
        admin.setStatus("INACTIVE");
        int adminId = genericDAO.update(admin);
        System.out.println("update admin id: " + adminId + ", status: " + admin.getStatus());
        System.out.println("---------------------------");
        return adminId;
    }

}
