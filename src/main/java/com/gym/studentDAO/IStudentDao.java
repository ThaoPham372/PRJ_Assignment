package com.gym.studentDAO;

import java.util.List;

import com.gym.model.Student;

/**
 * IStudentDao - Interface for Student Data Access Object
 * Defines methods for accessing student data from database
 */
public interface IStudentDao {
  /**
   * Get all students from the database
   * 
   * @return List of Student objects
   */
  List<Student> getAllStudents();

  /**
   * Get student by ID
   * 
   * @param studentId Student ID
   * @return Student object or null if not found
   */
  Student getStudentById(Long studentId);

  /**
   * Get students by package name
   * 
   * @param packageName Package name to filter
   * @return List of Student objects
   */

  /**
   * Search students by name, phone, or email
   * 
   * @param searchTerm Search term
   * @return List of Student objects matching the search
   */
  List<Student> searchStudents(String searchTerm);
}

