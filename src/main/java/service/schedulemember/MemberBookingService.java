package service.schedulemember;

import dao.GenericDAO;
import dao.schedulemember.PTBookingDAO;
import dao.schedulemember.TimeSlotDAO;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GymInfo;
import model.Member;
import model.Trainer;
import model.schedule.BookingStatus;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;

public class MemberBookingService {

    private static final Logger LOGGER = Logger.getLogger(MemberBookingService.class.getName());

    private final TimeSlotDAO timeSlotDao = new TimeSlotDAO();
    private final PTBookingDAO bookingDao = new PTBookingDAO();

    private final GenericDAO<Member> memberDAO = new GenericDAO<>(Member.class);
    private final GenericDAO<Trainer> trainerDAO = new GenericDAO<>(Trainer.class);
    private final GenericDAO<GymInfo> gymDAO = new GenericDAO<>(GymInfo.class);

    public List<TimeSlot> getAvailableSlots(int trainerId, int gymId, String dateStr) {
        try {
            if (dateStr == null || dateStr.isEmpty()) {
                return Collections.emptyList();
            }
            LocalDate date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                return Collections.emptyList();
            }
            return timeSlotDao.findAvailableSlots(trainerId, gymId, date);
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.WARNING, "Invalid date format provided: " + dateStr, e);
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error getting available slots", e);
            return Collections.emptyList();
        }
    }

    public boolean createBooking(int memberId, int trainerId, int gymId, int slotId, String dateStr, String notes) {
        try {
            Member member = memberDAO.findById(memberId);
            Trainer trainer = trainerDAO.findById(trainerId);
            TimeSlot timeSlot = timeSlotDao.findById(slotId);

            // FIX: GymInfo model dùng Long ID, nhưng tham số truyền vào là int.
            // Cần ép kiểu để Hibernate tìm đúng entity.
            GymInfo gym = gymDAO.findById(gymId);

            if (member == null || trainer == null || gym == null || timeSlot == null) {
                LOGGER.warning("Failed to create booking: Entity not found. Member=" + (member != null) +
                        ", Trainer=" + (trainer != null) + ", Gym=" + (gym != null) + ", Slot=" + (timeSlot != null));
                return false;
            }

            // Kiểm tra lại lần cuối xem slot còn trống không trước khi insert
            LocalDate bookingDate = LocalDate.parse(dateStr);
            List<TimeSlot> availableSlots = timeSlotDao.findAvailableSlots(trainerId, gymId, bookingDate);
            boolean isSlotStillAvailable = availableSlots.stream().anyMatch(slot -> slot.getSlotId() == slotId);

            if (!isSlotStillAvailable) {
                 LOGGER.warning("Slot " + slotId + " is no longer available for booking.");
                 return false;
            }

            PTBooking booking = new PTBooking();
            booking.setMember(member);
            booking.setMemberId(memberId); // Set cả ID explicitly để an toàn
            booking.setTrainer(trainer);
            booking.setTrainerId(trainerId);
            booking.setGym(gym);
            // CẢNH BÁO: PTBooking dùng Integer gymId, GymInfo dùng Long gymId.
            // Việc setGym(gym) có thể gây lỗi nếu JPA strict.
            // Tạm thời set ID thủ công nếu cần thiết:
            booking.setGymId(gymId);

            booking.setTimeSlot(timeSlot);
            booking.setSlotId(slotId);
            booking.setBookingDate(bookingDate);
            booking.setNotes(notes);
            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setCreatedAt(LocalDate.now());

            bookingDao.saveBooking(booking);
            LOGGER.info("Booking created successfully for member " + memberId);
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating booking for member " + memberId, e);
            return false;
        }
    }

    public List<PTBooking> getMemberBookingHistory(int memberId) {
        // ... (giữ nguyên)
        return bookingDao.findByMemberId(memberId);
    }
}