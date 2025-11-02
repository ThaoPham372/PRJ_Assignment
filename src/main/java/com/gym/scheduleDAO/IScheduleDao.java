package com.gym.scheduleDAO;

import com.gym.model.Schedule;
import java.util.List;

public interface IScheduleDao {
    // CRUD
    void insert(Schedule schedule) throws Exception;
    void update(Schedule schedule) throws Exception;
    void delete(int id) throws Exception;
    Schedule getById(int id) throws Exception;
    List<Schedule> getAll() throws Exception;
    List<Schedule> getByTrainerId(int trainerId) throws Exception;
    // Thay đổi trạng thái
    void updateStatus(int id, String status) throws Exception;
}


