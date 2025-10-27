package com.gym.dao;

import com.gym.model.Admin;
import java.sql.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class AdminDAO implements IAdminDAO{
    private final String Q_FIND_ALL = "SELECT a FROM Admin a";
    private final String Q_FIND_BY_NAME = "SELECT a FROM Admin a WHERE a.name = :name";
    private final String Q_FIND_BY_EMAIL = "SELECT a FROM Admin a WHERE a.email = :email";
    private final String Q_FIND_BY_NAME_OR_EMAIL = "SELECT a FROM Admin a WHERE a.name = :name OR a.email = :email";
    
    private final String Q_UPDATE_LAST_LOGIN = "UPDATE Admin a SET a.lastLogin = CURRENT_TIMESTAMP WHERE a.id = :id";
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GymPU");

    
    @Override
    public List<Admin> findAll() {
        EntityManager entityManager = emf.createEntityManager();
        try{
            return entityManager.createQuery(Q_FIND_ALL, Admin.class)
                     .getResultList();
        } catch (Exception e) {
            System.out.println("Error Admin: Find All -> " + e.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public Admin findById(long id) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            return entityManager.find(Admin.class, id);
        } catch (Exception e) {
            System.out.println("Error Admin: Find By Id -> " + e.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public Admin findByName(String name) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            return entityManager.createQuery(Q_FIND_BY_NAME, Admin.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Error Admin: Find By Name -> " + e.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public Admin findByEmail(String email) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            return entityManager.createQuery(Q_FIND_BY_EMAIL, Admin.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Error Admin: Find By Email -> " + e.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public boolean add(Admin admin) {
        EntityTransaction entityTransaction = null;
        EntityManager entityManger = emf.createEntityManager();
        try{
            entityTransaction = entityManger.getTransaction();
            entityTransaction.begin();
            entityManger.persist(admin);
            entityTransaction.commit();
        }
        catch (Exception e){
            if(entityTransaction != null) entityTransaction.rollback();
            System.out.println("Error Admin: Add Admin -> " + e.getMessage());
            return false;
        } finally {
            entityManger.close();
        }
        return true;
    }

    @Override
    public boolean update(Admin admin) {
        EntityTransaction entityTransaction = null;
        EntityManager entityManger = emf.createEntityManager();
        try{
            entityTransaction = entityManger.getTransaction();
            entityTransaction.begin();
            entityManger.merge(admin);
            entityTransaction.commit();
        }
        catch (Exception e){
            if(entityTransaction != null) entityTransaction.rollback();
            System.out.println("Error Admin: Update Admin -> " + e.getMessage());
            return false;
        } finally {
            entityManger.close();
        }
        return true;
    }

    @Override
    public boolean delete(Admin admin) {
        admin.setStatus("inactive");
        return this.update(admin);
    }
    
    @Override
    public boolean existsByName(String name) {
        Admin admins = this.findByName(name);
        return admins != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        Admin admins = this.findByEmail(email);
        return admins != null;
    }
    
    @Override
    public Admin findByNameOrEmail(String nameOrEmail) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            List<Admin> admins = entityManager.createQuery(Q_FIND_BY_NAME_OR_EMAIL, Admin.class)
                                    .setParameter("name", nameOrEmail)
                                    .setParameter("email", nameOrEmail)
                                    .getResultList();
            if(admins != null && !admins.isEmpty())
                return admins.get(0);
        } 
        catch (Exception e) {
            System.out.println("Error Admin: Find By Name or Email -> " + e.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public boolean incrementFailedLoginAttempts(Admin admin) {
        admin.setFailedLoginAttempts(admin.getFailedLoginAttempts() + 1);
        return this.update(admin);
    }
    
    @Override
    public boolean resetFailedLoginAttempts(Admin admin) {
        admin.setFailedLoginAttempts(0);
        if(!this.resetLockedUntil(admin)) return false;
        return this.update(admin);
    }
    
    @Override
    public boolean resetLockedUntil(Admin admin) {
        admin.setLockedUntil(null);
        return this.update(admin);
    }
  
    @Override
    public boolean lockAccount(Admin admin, int minutes) {
        Date newDate = new Date(admin.getLockedUntil().getTime() + minutes * 60 * 1000);
        admin.setLockedUntil(newDate);
        return this.update(admin);
    }
    
    @Override
    public boolean updateLastLogin(long adminId) {
        EntityTransaction entityTransaction = null;
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            
            Query query = entityManager.createQuery(Q_UPDATE_LAST_LOGIN);
            query.setParameter("id", adminId);
            query.executeUpdate();
            
            entityTransaction.commit();
        } 
        catch (Exception e) {
            if(entityTransaction != null) entityTransaction.rollback();
            System.out.println("Error Admin: Update Last Login -> " + e.getMessage());
            return false;
        } finally {
            entityManager.close();
        }
        return true;
    }
}
