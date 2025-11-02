package com.gym.service;

import java.util.List;

import com.gym.model.Schedule;
import com.gym.scheduleDAO.IScheduleDao;
import com.gym.scheduleDAO.ScheduleDao;

public class ScheduleServiceImpl implements ScheduleService {
  private IScheduleDao scheduleDao;

  public ScheduleServiceImpl() {
    this.scheduleDao = new ScheduleDao();
  }

  @Override
  public void createSchedule(Schedule schedule) throws Exception {
    scheduleDao.insert(schedule);
  }

  @Override
  public void updateSchedule(Schedule schedule) throws Exception {
    scheduleDao.update(schedule);
  }

  @Override
  public void deleteSchedule(int id) throws Exception {
    scheduleDao.delete(id);
  }

  @Override
  public Schedule getScheduleById(int id) throws Exception {
    return scheduleDao.getById(id);
  }

  @Override
  public List<Schedule> getAllSchedules() throws Exception {
    return scheduleDao.getAll();
  }

  @Override
  public List<Schedule> getSchedulesByTrainer(int trainerId) throws Exception {
    return scheduleDao.getByTrainerId(trainerId);
  }

  @Override
  public void updateStatus(int id, String status) throws Exception {
    scheduleDao.updateStatus(id, status);
  }
}
