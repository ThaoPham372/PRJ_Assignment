package com.gym.dao;

import java.util.List;
import java.util.Optional;

import com.gym.model.Student;

public interface IStudentDAO {

  Student findActiveByUsername(String username);

  Student findActiveByUsernameOrName(String input);

  Optional<Student> findByUserId(int userId);

  List<Student> findAllActive();

  // Search by a single keyword across name, username, phone, and training package
  List<Student> search(String keyword);

  List<Student> search(String keyword, String trainingPackage);

  Student update(Student student);
}
