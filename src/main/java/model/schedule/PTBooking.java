package model.schedule;

import model.Member;
import model.Trainer;
import model.GymInfo;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * PTBooking model - Represents member's PT booking
 * Maps to pt_bookings table
 */
@Entity
@Table(name = "pt_bookings")
public class PTBooking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;
    
    @Column(name = "member_id", nullable = false)
    private Integer memberId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;
    
    @Column(name = "trainer_id", nullable = false)
    private Integer trainerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", insertable = false, updatable = false)
    private Trainer trainer;
    
    @Column(name = "gym_id", nullable = false)
    private Integer gymId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", insertable = false, updatable = false)
    private GymInfo gym;
    
    @Column(name = "slot_id", nullable = false)
    private Integer slotId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", length = 20)
    private BookingStatus bookingStatus;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "cancelled_reason", length = 500)
    private String cancelledReason;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_by", length = 20)
    private CancelledBy cancelledBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    
    @Column(name = "confirmed_at")
    private LocalDate confirmedAt;
    
    @Column(name = "completed_at")
    private LocalDate completedAt;
    
    @Column(name = "cancelled_at")
    private LocalDate cancelledAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
        if (bookingStatus == null) {
            bookingStatus = BookingStatus.PENDING;
        }
    }

    public PTBooking() {
    }

    // Getters and Setters
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    public GymInfo getGym() {
        return gym;
    }

    public void setGym(GymInfo gym) {
        this.gym = gym;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public CancelledBy getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(CancelledBy cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDate confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDate getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDate cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}

