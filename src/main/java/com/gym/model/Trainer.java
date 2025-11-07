package com.gym.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * Trainer entity representing extended trainer-specific information.
 * Inherits from User using JOINED inheritance strategy.
 * Links to User entity via user_id (shared primary key).
 */
@Entity
@Table(name = "trainer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainer extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "specialization", length = 500)
    private String specialization;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "certification_level", length = 100)
    private String certificationLevel;

    @Column(name = "students_count")
    private Integer studentsCount;

    @Column(name = "average_rating", precision = 3, scale = 1)
    private BigDecimal averageRating;

    @Column(name = "sessions_this_month")
    private Integer sessionsThisMonth;

    @Column(name = "cancelled_sessions_this_month")
    private Integer cancelledSessionsThisMonth;

    @Column(name = "total_hours_trained")
    private Integer totalHoursTrained;

    @Column(name = "average_rating_this_month", precision = 3, scale = 1)
    private BigDecimal averageRatingThisMonth;

    @Column(name = "completion_rate", precision = 3, scale = 2)
    private BigDecimal completionRate;

    @Column(name = "new_students_this_month")
    private Integer newStudentsThisMonth;

    public Trainer() {
        super();
    }

    public Trainer(Integer userId) {
        super();
    }

    // Getters & Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }

    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(Integer studentsCount) {
        this.studentsCount = studentsCount;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getSessionsThisMonth() {
        return sessionsThisMonth;
    }

    public void setSessionsThisMonth(Integer sessionsThisMonth) {
        this.sessionsThisMonth = sessionsThisMonth;
    }

    public Integer getCancelledSessionsThisMonth() {
        return cancelledSessionsThisMonth;
    }

    public void setCancelledSessionsThisMonth(Integer cancelledSessionsThisMonth) {
        this.cancelledSessionsThisMonth = cancelledSessionsThisMonth;
    }

    public Integer getTotalHoursTrained() {
        return totalHoursTrained;
    }

    public void setTotalHoursTrained(Integer totalHoursTrained) {
        this.totalHoursTrained = totalHoursTrained;
    }

    public BigDecimal getAverageRatingThisMonth() {
        return averageRatingThisMonth;
    }

    public void setAverageRatingThisMonth(BigDecimal averageRatingThisMonth) {
        this.averageRatingThisMonth = averageRatingThisMonth;
    }

    public BigDecimal getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }

    public Integer getNewStudentsThisMonth() {
        return newStudentsThisMonth;
    }

    public void setNewStudentsThisMonth(Integer newStudentsThisMonth) {
        this.newStudentsThisMonth = newStudentsThisMonth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getUserId() != null ? this.getUserId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Trainer)) {
            return false;
        }
        Trainer other = (Trainer) object;
        if ((this.getUserId() == null && other.getUserId() != null) || 
            (this.getUserId() != null && !this.getUserId().equals(other.getUserId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Trainer{" + 
               "id=" + this.getUserId() + 
               ", specialization=" + specialization + 
               ", yearsOfExperience=" + yearsOfExperience + 
               ", certificationLevel=" + certificationLevel + 
               ", studentsCount=" + studentsCount + 
               ", averageRating=" + averageRating + 
               '}';
    }
}
