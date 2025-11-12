package model.schedule;

import model.Trainer;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * TrainerException model - Represents trainer's exception schedule (off/busy/special)
 * Maps to trainer_exceptions table
 */
@Entity
@Table(name = "trainer_exceptions")
public class TrainerException {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exception_id")
    private Integer exceptionId;
    
    @Column(name = "trainer_id", nullable = false)
    private Integer trainerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", insertable = false, updatable = false)
    private Trainer trainer;
    
    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;
    
    @Column(name = "slot_id")
    private Integer slotId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    private TimeSlot timeSlot;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type", nullable = false, length = 20)
    private ExceptionType exceptionType;
    
    @Column(name = "reason", length = 255)
    private String reason;
    
    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    public TrainerException() {
    }

    // Getters and Setters
    public Integer getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Integer exceptionId) {
        this.exceptionId = exceptionId;
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

    public LocalDate getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
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

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

