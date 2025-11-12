package model.schedule;

import model.Trainer;
import model.GymInfo;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * TrainerSchedule model - Represents trainer's weekly schedule
 * Maps to trainer_schedules table
 */
@Entity
@Table(name = "trainer_schedules")
public class TrainerSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "slot_id", nullable = false)
    private Integer slotId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @Column(name = "max_bookings")
    private Integer maxBookings;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDate.now();
        }
        if (isAvailable == null) {
            isAvailable = true;
        }
        if (maxBookings == null) {
            maxBookings = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public TrainerSchedule() {
    }

    // Getters and Setters
    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getMaxBookings() {
        return maxBookings;
    }

    public void setMaxBookings(Integer maxBookings) {
        this.maxBookings = maxBookings;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}

