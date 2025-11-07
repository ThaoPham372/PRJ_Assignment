package com.gym.service;

import java.util.List;
import java.util.Optional;

import com.gym.model.Student;

public interface IStudentService {

  List<Student> getAllActiveStudents();

  List<Student> searchStudents(String keyword);

  List<Student> searchStudents(String keyword, String trainingPackage);

  Optional<Student> getStudentById(Integer userId);

  Student updateStudent(Student student);

  int getTotalStudentsCount();

  int getActiveStudentsCount();

  int getAchievedGoalCount();
}
