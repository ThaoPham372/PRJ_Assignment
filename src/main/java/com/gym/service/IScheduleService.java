package com.gym.service;

import java.util.List;

import com.gym.model.Schedule;
import com.gym.model.ScheduleStatus;

public interface IScheduleService {

  Schedule createWithUsername(Schedule schedule, String username);

  Schedule create(Schedule schedule);

  Schedule update(Schedule schedule);

  List<Schedule> listAllSchedules();

  List<Schedule> findByTrainerAndMonth(int trainerId, int year, int month);

  java.util.Map<String, java.util.List<Schedule>> getSchedulesByDateForMonth(int year, int month);

  void updateStatus(int id, ScheduleStatus status);

  void delete(int id);

  Schedule getById(int id);

  java.util.List<java.time.YearMonth> getMonthsWindowAroundNow();

  java.util.Map<String, java.util.List<Schedule>> getSchedulesByDateForMonthWithinWindow(int year, int month);
}
