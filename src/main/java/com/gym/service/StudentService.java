package com.gym.service;

import java.util.List;
import java.util.Optional;

import com.gym.dao.IStudentDAO;
import com.gym.dao.StudentDAO;
import com.gym.model.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class StudentService implements IStudentService {

  private final IStudentDAO studentDAO;

  public StudentService() {
    this.studentDAO = new StudentDAO();
  }

  public StudentService(IStudentDAO studentDAO) {
    this.studentDAO = studentDAO;
  }

  @Override
  public List<Student> getAllActiveStudents() {
    return studentDAO.findAllActive();
  }

  @Override
  public List<Student> searchStudents(String keyword, String trainingPackage) {
    return studentDAO.search(keyword, trainingPackage);
  }

  @Override
  public List<Student> searchStudents(String keyword) {
    return studentDAO.search(keyword);
  }

  @Override
  public Optional<Student> getStudentById(Integer userId) {
    if (userId == null) {
      return Optional.empty();
    }
    return studentDAO.findByUserId(userId);
  }

  @Override
  public Student updateStudent(Student student) {
    if (student == null || student.getUserId() == null) {
      throw new IllegalArgumentException("Student and userId cannot be null");
    }
    // Ensure user and student update happen together where possible
    return studentDAO.update(student);
  }

  @Override
  public int getTotalStudentsCount() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym-pu");
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(s) FROM Student s JOIN s.user u WHERE u.dtype = 'User' AND u.status = 'ACTIVE'",
          Long.class);
      return query.getSingleResult().intValue();
    } catch (Exception e) {
      System.err.println("Error getting total students count: " + e.getMessage());
      return 0;
    } finally {
      em.close();
    }
  }

  @Override
  public int getActiveStudentsCount() {
    // Same as total for now, but can be extended to filter by active training
    // sessions
    return getTotalStudentsCount();
  }

  @Override
  public int getAchievedGoalCount() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym-pu");
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(s) FROM Student s JOIN s.user u WHERE u.dtype = 'User' AND u.status = 'ACTIVE' AND s.trainingProgress >= 100",
          Long.class);
      return query.getSingleResult().intValue();
    } catch (Exception e) {
      System.err.println("Error getting achieved goal count: " + e.getMessage());
      return 0;
    } finally {
      em.close();
    }
  }
}
