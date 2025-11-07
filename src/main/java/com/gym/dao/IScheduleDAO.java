package com.gym.dao;

import java.util.List;

import com.gym.model.Schedule;
import com.gym.model.ScheduleStatus;

public interface IScheduleDAO {

  Schedule save(Schedule schedule);

  Schedule create(Schedule schedule);

  Schedule update(Schedule schedule);

  List<Schedule> findAll();

  List<Schedule> findByDateRange(java.time.LocalDate start, java.time.LocalDate end);

  List<Schedule> findByTrainerAndMonth(int trainerId, int year, int month);

  Schedule findById(Integer id);

  void updateStatus(Integer id, ScheduleStatus status);

  void deleteById(Integer id);
}
