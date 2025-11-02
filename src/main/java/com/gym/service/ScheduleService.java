package com.gym.service;

import java.util.List;

import com.gym.model.Schedule;

public interface ScheduleService {
  void createSchedule(Schedule schedule) throws Exception;

  void updateSchedule(Schedule schedule) throws Exception;

  void deleteSchedule(int id) throws Exception;

  Schedule getScheduleById(int id) throws Exception;

  List<Schedule> getAllSchedules() throws Exception;

  List<Schedule> getSchedulesByTrainer(int trainerId) throws Exception;

  void updateStatus(int id, String status) throws Exception;
}
