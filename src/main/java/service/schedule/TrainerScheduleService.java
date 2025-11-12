package service.schedule;

import java.time.LocalDate;
import java.util.List;

import dao.schedule.TimeSlotDAO;
import dao.schedule.TrainerScheduleDAO;
import model.schedule.BookingStatus;
import model.schedule.ExceptionType;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import model.schedule.TrainerException;
import model.schedule.TrainerSchedule;

public class TrainerScheduleService {

    private final TrainerScheduleDAO dao;

    public TrainerScheduleService() {
        this.dao = new TrainerScheduleDAO();
    }

    // Lấy danh sách buổi tập
    public List<PTBooking> getTrainerBookings(int trainerId) {
        return dao.getBookingsByTrainer(trainerId);
    }

    // Cập nhật trạng thái buổi tập
    public void updateBookingStatus(int bookingId, BookingStatus status) {
        dao.updateBookingStatus(bookingId, status);
    }

    // Lấy lịch định kỳ
    public List<TrainerSchedule> getWeeklySchedule(int trainerId) {
        return dao.getWeeklySchedule(trainerId);
    }

    // Lấy danh sách ngày nghỉ/bận
    public List<TrainerException> getTrainerExceptions(int trainerId) {
        return dao.getTrainerExceptions(trainerId);
    }

    // Thêm ngày nghỉ/bận
    public void addException(int trainerId, LocalDate date, int slotId, ExceptionType type, String reason) {
        dao.addException(trainerId, date, slotId, type, reason);
    }

    // Xóa ngày nghỉ/bận
    public void deleteException(int exceptionId) {
        dao.deleteException(exceptionId);
    }

    // Lấy danh sách time slots
    public List<TimeSlot> getActiveTimeSlots() {
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        return timeSlotDAO.findActiveSlots();
    }

    public List<Object[]> getWeeklyFixedSchedule(int trainerId, java.time.LocalDate monday) {
        var start = java.sql.Date.valueOf(monday);
        var end = java.sql.Date.valueOf(monday.plusDays(6));
        return dao.getWeeklyFixedSchedule(trainerId, start, end);
    }

    public List<Object[]> getTimeSlotsBasic() {
        return dao.getTimeSlotsBasic();
    }

    // CRUD operations for TrainerSchedule
    public void createSchedule(TrainerSchedule schedule) {
        dao.saveSchedule(schedule);
    }

    public TrainerSchedule getScheduleById(int scheduleId) {
        return dao.getScheduleById(scheduleId);
    }

    public void updateSchedule(TrainerSchedule schedule) {
        dao.updateSchedule(schedule);
    }

    public void deleteSchedule(TrainerSchedule schedule) {
        dao.deleteSchedule(schedule);
    }

    public List<TrainerSchedule> getAvailableSchedulesByTrainer(int trainerId) {
        return dao.getAvailableSchedulesByTrainer(trainerId);
    }
}
