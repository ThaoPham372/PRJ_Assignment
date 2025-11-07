package com.gym.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import com.gym.dao.IScheduleDAO;
import com.gym.dao.IStudentDAO;
import com.gym.model.Schedule;
import com.gym.model.ScheduleStatus;
import com.gym.model.Student;

public class ScheduleService implements IScheduleService {

  private final IScheduleDAO scheduleDAO;
  private final IStudentDAO studentDAO;

  public ScheduleService(IScheduleDAO scheduleDAO, IStudentDAO studentDAO) {
    this.scheduleDAO = scheduleDAO;
    this.studentDAO = studentDAO;
  }

  @Override
  @Transactional
  public Schedule createWithUsername(Schedule schedule, String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username is required");
    }

    Student student = studentDAO.findActiveByUsername(username.trim());
    if (student == null) {
      throw new IllegalArgumentException("Student not found or inactive: " + username);
    }

    schedule.setStudent(student);
    if (schedule.getStatus() == null) {
      schedule.setStatus(ScheduleStatus.pending);
    }
    Timestamp now = Timestamp.from(Instant.now());
    schedule.setCreatedAt(now);
    schedule.setUpdatedAt(now);
    return scheduleDAO.save(schedule);
  }

  @Override
  @Transactional
  public Schedule create(Schedule schedule) {
    if (schedule.getStudent() == null) {
      throw new IllegalArgumentException("Student is required");
    }
    if (schedule.getStatus() == null) {
      schedule.setStatus(ScheduleStatus.pending);
    }
    Timestamp now = Timestamp.from(Instant.now());
    schedule.setCreatedAt(now);
    schedule.setUpdatedAt(now);
    return scheduleDAO.create(schedule);
  }

  @Override
  @Transactional
  public Schedule update(Schedule schedule) {
    if (schedule.getId() == null) {
      throw new IllegalArgumentException("Schedule id is required for update");
    }
    schedule.setUpdatedAt(Timestamp.from(Instant.now()));
    return scheduleDAO.update(schedule);
  }

  public List<Schedule> listAllSchedules() {
    return scheduleDAO.findAll();
  }

  public List<Schedule> findByTrainerAndMonth(int trainerId, int year, int month) {
    return scheduleDAO.findByTrainerAndMonth(trainerId, year, month);
  }

  public Map<String, List<Schedule>> getSchedulesByDateForMonth(int year, int month) {
    YearMonth ym = YearMonth.of(year, month);
    LocalDate start = ym.atDay(1);
    LocalDate end = ym.atEndOfMonth();
    List<Schedule> items = scheduleDAO.findByDateRange(start, end);

    Map<String, List<Schedule>> byDate = new LinkedHashMap<>();
    for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
      byDate.put(d.toString(), new java.util.ArrayList<>());
    }
    for (Schedule s : items) {
      String key = s.getTrainingDate().toString();
      byDate.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(s);
    }
    return byDate;
  }

  @Transactional
  public void updateStatus(int id, ScheduleStatus status) {
    if (status == null) {
      throw new IllegalArgumentException("Status is required");
    }
    scheduleDAO.updateStatus(id, status);
  }

  @Transactional
  public void delete(int id) {
    scheduleDAO.deleteById(id);
  }

  @Override
  public Schedule getById(int id) {
    return scheduleDAO.findById(id);
  }

  public List<YearMonth> getMonthsWindowAroundNow() {
    YearMonth center = YearMonth.from(LocalDate.now());
    List<YearMonth> months = new java.util.ArrayList<>();
    for (int i = -3; i <= 3; i++) {
      months.add(center.plusMonths(i));
    }
    return months;
  }

  public Map<String, List<Schedule>> getSchedulesByDateForMonthWithinWindow(int year, int month) {
    YearMonth target = YearMonth.of(year, month);
    YearMonth nowYm = YearMonth.from(LocalDate.now());
    if (target.isBefore(nowYm.minusMonths(3)) || target.isAfter(nowYm.plusMonths(3))) {
      throw new IllegalArgumentException("Month out of allowed range (±3 months)");
    }
    return getSchedulesByDateForMonth(year, month);
  }
}
