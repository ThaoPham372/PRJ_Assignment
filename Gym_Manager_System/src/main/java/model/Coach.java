package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Coach model - Đại diện cho huấn luyện viên/nhân viên phòng gym
 */
public class Coach {
    private int coachId;
    private int userId; // Foreign key to User
    
    // Thông tin cơ bản
    private String employeeCode; // Mã nhân viên (VD: COACH2024001)
    private String position; // personal_trainer, group_instructor, manager, receptionist
    private String department; // training, management, administration
    private Date hireDate; // Ngày gia nhập
    private String employmentType; // full_time, part_time, contract, intern
    private String status; // active, on_leave, suspended, terminated
    
    // Thông tin chuyên môn
    private String specialization; // weight_training, cardio, yoga, pilates, boxing, nutrition
    private String certifications; // Các chứng chỉ (JSON format hoặc comma-separated)
    private Integer yearsOfExperience;
    private String education; // Trình độ học vấn
    private String languages; // Ngôn ngữ (Vietnamese, English, etc.)
    
    // Thông tin lương và hợp đồng
    private BigDecimal baseSalary; // Lương cơ bản
    private BigDecimal hourlyRate; // Lương theo giờ (cho part-time)
    private BigDecimal commission; // Hoa hồng (%)
    private BigDecimal bonus; // Thưởng
    private String paymentFrequency; // monthly, bi_weekly, weekly
    private Date contractStartDate;
    private Date contractEndDate;
    
    // Thống kê làm việc
    private Integer totalClientsAssigned; // Tổng số khách hàng được giao
    private Integer activeClients; // Số khách hàng đang active
    private Integer totalSessions; // Tổng số buổi tập
    private Integer completedSessions; // Số buổi đã hoàn thành
    private Integer cancelledSessions; // Số buổi bị hủy
    private BigDecimal totalRevenue; // Tổng doanh thu mang lại
    private Double averageRating; // Đánh giá trung bình (1-5 sao)
    private Integer totalReviews; // Tổng số đánh giá
    
    // Lịch làm việc
    private String workingDays; // JSON array: ["Monday", "Wednesday", "Friday"]
    private String workingHours; // VD: "08:00-17:00"
    private Integer maxClientsPerDay; // Số khách tối đa/ngày
    private Boolean availableForNewClients;
    
    // Thông tin liên hệ khẩn cấp
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    
    // Thông tin ngân hàng
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountName;
    
    // Đánh giá hiệu suất
    private Double performanceScore; // 0-100
    private String performanceNotes;
    private Date lastPerformanceReview;
    private Date nextPerformanceReview;
    
    // Ghi chú và thông tin khác
    private String bio; // Tiểu sử ngắn
    private String achievements; // Thành tích
    private String notes;
    private String profileImage;
    
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    // Constructors
    public Coach() {
        this.hireDate = new Date();
        this.status = "active";
        this.totalClientsAssigned = 0;
        this.activeClients = 0;
        this.totalSessions = 0;
        this.completedSessions = 0;
        this.cancelledSessions = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.averageRating = 0.0;
        this.totalReviews = 0;
        this.availableForNewClients = true;
        this.performanceScore = 0.0;
    }

    public Coach(int userId, String employeeCode, String position) {
        this();
        this.userId = userId;
        this.employeeCode = employeeCode;
        this.position = position;
    }

    // Getters and Setters
    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Integer getTotalClientsAssigned() {
        return totalClientsAssigned;
    }

    public void setTotalClientsAssigned(Integer totalClientsAssigned) {
        this.totalClientsAssigned = totalClientsAssigned;
    }

    public Integer getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(Integer activeClients) {
        this.activeClients = activeClients;
    }

    public Integer getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Integer totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Integer getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(Integer completedSessions) {
        this.completedSessions = completedSessions;
    }

    public Integer getCancelledSessions() {
        return cancelledSessions;
    }

    public void setCancelledSessions(Integer cancelledSessions) {
        this.cancelledSessions = cancelledSessions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public Integer getMaxClientsPerDay() {
        return maxClientsPerDay;
    }

    public void setMaxClientsPerDay(Integer maxClientsPerDay) {
        this.maxClientsPerDay = maxClientsPerDay;
    }

    public Boolean getAvailableForNewClients() {
        return availableForNewClients;
    }

    public void setAvailableForNewClients(Boolean availableForNewClients) {
        this.availableForNewClients = availableForNewClients;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public Double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(Double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public String getPerformanceNotes() {
        return performanceNotes;
    }

    public void setPerformanceNotes(String performanceNotes) {
        this.performanceNotes = performanceNotes;
    }

    public Date getLastPerformanceReview() {
        return lastPerformanceReview;
    }

    public void setLastPerformanceReview(Date lastPerformanceReview) {
        this.lastPerformanceReview = lastPerformanceReview;
    }

    public Date getNextPerformanceReview() {
        return nextPerformanceReview;
    }

    public void setNextPerformanceReview(Date nextPerformanceReview) {
        this.nextPerformanceReview = nextPerformanceReview;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Business logic methods
    public BigDecimal calculateMonthlySalary() {
        BigDecimal total = baseSalary != null ? baseSalary : BigDecimal.ZERO;
        if (bonus != null) {
            total = total.add(bonus);
        }
        return total;
    }

    public Double getCompletionRate() {
        if (totalSessions == null || totalSessions == 0) return 0.0;
        return (completedSessions.doubleValue() / totalSessions.doubleValue()) * 100;
    }

    public boolean isContractActive() {
        if (contractEndDate == null) return true; // Permanent contract
        return contractEndDate.after(new Date());
    }

    public void incrementSessionCount() {
        this.totalSessions++;
    }

    public void incrementCompletedSession() {
        this.completedSessions++;
    }

    public void addClient() {
        this.totalClientsAssigned++;
        this.activeClients++;
    }

    public void removeClient() {
        if (this.activeClients > 0) {
            this.activeClients--;
        }
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachId=" + coachId +
                ", employeeCode='" + employeeCode + '\'' +
                ", position='" + position + '\'' +
                ", specialization='" + specialization + '\'' +
                ", status='" + status + '\'' +
                ", activeClients=" + activeClients +
                '}';
    }
}

