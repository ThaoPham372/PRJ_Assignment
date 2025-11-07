package com.gym.dao;

import java.util.List;
import java.util.Optional;

import com.gym.model.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class StudentDAO implements IStudentDAO {

  private final EntityManagerFactory entityManagerFactory;

  public StudentDAO() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
  }

  @Override
  public Student findActiveByUsername(String username) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      return em.createQuery(
          "SELECT s FROM Student s WHERE s.user.username = :username AND s.user.role = 'USER' AND s.user.status = 'ACTIVE'",
          Student.class)
          .setParameter("username", username)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } finally {
      em.close();
    }
  }

  @Override
  public Student findActiveByUsernameOrName(String input) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Ưu tiên tìm theo username trước (vì username là unique)
      try {
        return em.createQuery(
            "SELECT s FROM Student s WHERE s.user.username = :input AND s.user.dtype = 'User' AND s.user.status = 'ACTIVE'",
            Student.class)
            .setParameter("input", input)
            .getSingleResult();
      } catch (NoResultException e) {
        // Nếu không tìm thấy theo username, tìm theo name
        // Lấy kết quả đầu tiên nếu có nhiều Student cùng tên
        java.util.List<Student> students = em.createQuery(
            "SELECT s FROM Student s WHERE s.user.name = :input AND s.user.dtype = 'User' AND s.user.status = 'ACTIVE'",
            Student.class)
            .setParameter("input", input)
            .getResultList();
        if (!students.isEmpty()) {
          return students.get(0);
        }
        return null;
      }
    } catch (Exception ex) {
      return null;
    } finally {
      em.close();
    }
  }

  @Override
  public Optional<Student> findByUserId(int userId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Join fetch to ensure user is loaded and to select by user_id key
      TypedQuery<Student> q = em.createQuery(
          "SELECT s FROM Student s JOIN FETCH s.user u WHERE u.userId = :uid", Student.class);
      q.setParameter("uid", userId);
      Student student = null;
      try {
        student = q.getSingleResult();
      } catch (NoResultException ex) {
        student = null;
      }
      return Optional.ofNullable(student);
    } finally {
      em.close();
    }
  }

  @Override
  public List<Student> findAllActive() {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<Student> query = em.createQuery(
          "SELECT s FROM Student s JOIN FETCH s.user u WHERE u.dtype = 'User' AND u.status = 'ACTIVE' ORDER BY u.name",
          Student.class);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error finding all active students: " + ex.getMessage());
      ex.printStackTrace();
      return new java.util.ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Student> search(String keyword, String trainingPackage) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      StringBuilder jpql = new StringBuilder(
          "SELECT s FROM Student s JOIN FETCH s.user u WHERE u.dtype = 'User' AND u.status = 'ACTIVE'");

      if (keyword != null && !keyword.trim().isEmpty()) {
        jpql.append(
            " AND (LOWER(u.name) LIKE :keyword OR LOWER(u.username) LIKE :keyword OR LOWER(u.phone) LIKE :keyword OR LOWER(s.trainingPackage) LIKE :keyword)");
      }

      if (trainingPackage != null && !trainingPackage.trim().isEmpty()) {
        jpql.append(" AND s.trainingPackage = :trainingPackage");
      }

      jpql.append(" ORDER BY u.name");

      TypedQuery<Student> query = em.createQuery(jpql.toString(), Student.class);

      if (keyword != null && !keyword.trim().isEmpty()) {
        String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
        query.setParameter("keyword", searchPattern);
      }

      if (trainingPackage != null && !trainingPackage.trim().isEmpty()) {
        query.setParameter("trainingPackage", trainingPackage.trim());
      }

      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error searching students: " + ex.getMessage());
      ex.printStackTrace();
      return new java.util.ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Student> search(String keyword) {
    return search(keyword, null);
  }

  @Override
  public Student update(Student student) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    try {
      transaction.begin();
      Student merged = em.merge(student);
      transaction.commit();
      return merged;
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      System.err.println("Error updating student: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Error updating student", e);
    } finally {
      em.close();
    }
  }
}
