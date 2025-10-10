package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * WorkoutSession model - Đại diện cho buổi tập của thành viên
 */
public class WorkoutSession {
    private int sessionId;
    private int memberId; // Foreign key to Member
    private Integer coachId; // Foreign key to Coach (nullable for self-training)
    
    // Thông tin buổi tập
    private String sessionType; // personal_training, group_class, self_training, online_training
    private Date sessionDate;
    private Date startTime;
    private Date endTime;
    private Integer durationMinutes;
    
    // Trạng thái
    private String status; // scheduled, in_progress, completed, cancelled, no_show
    private String cancellationReason;
    private Date cancelledAt;
    private String cancelledBy;
    
    // Chi tiết tập luyện
    private String workoutType; // cardio, strength, flexibility, hiit, crossfit, yoga, pilates
    private String intensity; // low, moderate, high, extreme
    private String focus; // upper_body, lower_body, full_body, core, specific_muscle_group
    
    // Thống kê
    private Integer caloriesBurned;
    private Double distance; // km
    private Integer heartRateAvg;
    private Integer heartRateMax;
    private Integer steps;
    
    // Ghi chú và đánh giá
    private String notes;
    private String coachNotes; // Ghi chú của PT
    private Integer memberRating; // 1-5 sao
    private String memberFeedback;
    private Integer coachRating; // Đánh giá của coach về member
    
    // Check-in/Check-out
    private Date checkInTime;
    private Date checkOutTime;
    private String checkInMethod; // qr_code, card, manual
    
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    
    // Constructors
    public WorkoutSession() {
        this.status = "scheduled";
        this.createdAt = new Date();
    }

    public WorkoutSession(int memberId, Date sessionDate, String sessionType) {
        this();
        this.memberId = memberId;
        this.sessionDate = sessionDate;
        this.sessionType = sessionType;
    }

    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
        calculateDuration();
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Date getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getHeartRateAvg() {
        return heartRateAvg;
    }

    public void setHeartRateAvg(Integer heartRateAvg) {
        this.heartRateAvg = heartRateAvg;
    }

    public Integer getHeartRateMax() {
        return heartRateMax;
    }

    public void setHeartRateMax(Integer heartRateMax) {
        this.heartRateMax = heartRateMax;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCoachNotes() {
        return coachNotes;
    }

    public void setCoachNotes(String coachNotes) {
        this.coachNotes = coachNotes;
    }

    public Integer getMemberRating() {
        return memberRating;
    }

    public void setMemberRating(Integer memberRating) {
        this.memberRating = memberRating;
    }

    public String getMemberFeedback() {
        return memberFeedback;
    }

    public void setMemberFeedback(String memberFeedback) {
        this.memberFeedback = memberFeedback;
    }

    public Integer getCoachRating() {
        return coachRating;
    }

    public void setCoachRating(Integer coachRating) {
        this.coachRating = coachRating;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCheckInMethod() {
        return checkInMethod;
    }

    public void setCheckInMethod(String checkInMethod) {
        this.checkInMethod = checkInMethod;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business logic methods
    private void calculateDuration() {
        if (startTime != null && endTime != null) {
            long diffInMillis = endTime.getTime() - startTime.getTime();
            this.durationMinutes = (int) (diffInMillis / (1000 * 60));
        }
    }

    public void checkIn() {
        this.checkInTime = new Date();
        this.status = "in_progress";
    }

    public void checkOut() {
        this.checkOutTime = new Date();
        this.status = "completed";
    }

    public void cancel(String reason, String cancelledBy) {
        this.status = "cancelled";
        this.cancellationReason = reason;
        this.cancelledAt = new Date();
        this.cancelledBy = cancelledBy;
    }

    public boolean isCompleted() {
        return "completed".equals(status);
    }

    @Override
    public String toString() {
        return "WorkoutSession{" +
                "sessionId=" + sessionId +
                ", memberId=" + memberId +
                ", sessionType='" + sessionType + '\'' +
                ", sessionDate=" + sessionDate +
                ", status='" + status + '\'' +
                '}';
    }
}

