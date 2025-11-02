package com.gym.service;

import java.util.List;

import com.gym.model.Student;
import com.gym.studentDAO.IStudentDao;
import com.gym.studentDAO.StudentDao;

/**
 * StudentServiceImpl - Implementation of StudentService
 * Contains business logic for student management
 */
public class StudentServiceImpl implements StudentService {

  private IStudentDao studentDao;

  public StudentServiceImpl() {
    this.studentDao = new StudentDao();
  }

  @Override
  public List<Student> getAllStudents() {
    try {
      return studentDao.getAllStudents();
    } catch (Exception e) {
      System.err.println("Error in StudentService.getAllStudents: " + e.getMessage());
      e.printStackTrace();
      return new java.util.ArrayList<>(); // Return empty list on error
    }
  }

  @Override
  public Student getStudentById(Long studentId) {
    try {
      if (studentId == null || studentId <= 0) {
        return null;
      }
      return studentDao.getStudentById(studentId);
    } catch (Exception e) {
      System.err.println("Error in StudentService.getStudentById: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

//  @Override
//  public List<Student> getStudentsByPackage(String packageName) {
//    try {
//      return studentDao.getStudentsByPackage(packageName);
//    } catch (Exception e) {
//      System.err.println("Error in StudentService.getStudentsByPackage: " + e.getMessage());
//      e.printStackTrace();
//      return new java.util.ArrayList<>(); // Return empty list on error
//    }
//  }

  @Override
  public List<Student> searchStudents(String searchTerm) {
    try {
      if (searchTerm == null || searchTerm.trim().isEmpty()) {
        return getAllStudents();
      }
      return studentDao.searchStudents(searchTerm.trim());
    } catch (Exception e) {
      System.err.println("Error in StudentService.searchStudents: " + e.getMessage());
      e.printStackTrace();
      return new java.util.ArrayList<>(); // Return empty list on error
    }
  }
}

