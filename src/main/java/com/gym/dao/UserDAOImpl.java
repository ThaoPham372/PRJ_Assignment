package com.gym.dao;

import java.util.Optional;

import com.gym.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class UserDAOImpl implements IUserDAO {

  private final EntityManagerFactory entityManagerFactory;

  public UserDAOImpl() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
  }

  @Override
  public Optional<User> findByUsernameOrNameAndDtypeAndStatus(String input) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Ưu tiên tìm theo username trước (vì username là unique)
      try {
        User user = em.createQuery(
            "SELECT u FROM User u WHERE u.username = :input AND u.dtype = 'User' AND u.status = 'ACTIVE'",
            User.class)
            .setParameter("input", input)
            .getSingleResult();
        return Optional.of(user);
      } catch (NoResultException e) {
        // Nếu không tìm thấy theo username, tìm theo name
        // Lấy kết quả đầu tiên nếu có nhiều User cùng tên
        java.util.List<User> users = em.createQuery(
            "SELECT u FROM User u WHERE u.name = :input AND u.dtype = 'User' AND u.status = 'ACTIVE'",
            User.class)
            .setParameter("input", input)
            .getResultList();
        if (!users.isEmpty()) {
          return Optional.of(users.get(0));
        }
        return Optional.empty();
      }
    } catch (Exception ex) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  @Override
  public Optional<User> findByUsernameOrNameAndRoleAndStatus(String input) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Ưu tiên tìm theo username trước (unique)
      try {
        User user = em.createQuery(
            "SELECT u FROM User u WHERE u.username = :input AND u.role = 'User' AND u.status = 'ACTIVE'",
            User.class)
            .setParameter("input", input)
            .getSingleResult();
        return Optional.of(user);
      } catch (NoResultException e) {
        // Nếu không tìm thấy theo username, tìm theo name
        java.util.List<User> users = em.createQuery(
            "SELECT u FROM User u WHERE u.name = :input AND u.role = 'User' AND u.status = 'ACTIVE'",
            User.class)
            .setParameter("input", input)
            .getResultList();
        if (!users.isEmpty()) {
          return Optional.of(users.get(0));
        }
        return Optional.empty();
      }
    } catch (Exception ex) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }
}
