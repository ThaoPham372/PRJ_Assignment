package com.gym.service;

import java.util.List;

import com.gym.model.Student;

/**
 * StudentService - Interface for Student business logic
 * Defines service methods for student management
 */
public interface StudentService {
  /**
   * Get all students
   * 
   * @return List of all students
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
   * @return List of students with the specified package
   */
//  List<Student> getStudentsByPackage(String packageName);

  /**
   * Search students by name, phone, or email
   * 
   * @param searchTerm Search term
   * @return List of matching students
   */
  List<Student> searchStudents(String searchTerm);

  /**
   * Get statistics for student management
   * 
   * @return Array with [totalStudents, activeStudents, achievedGoalCount]
   */
}

