package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * MembershipPackage model - Đại diện cho các gói thành viên
 */
public class MembershipPackage {
    private int packageId;
    
    // Thông tin cơ bản
    private String packageCode; // VD: BASIC, PREMIUM, VIP
    private String packageName;
    private String description;
    private String packageType; // basic, premium, vip, student, senior, family
    
    // Giá và thời hạn
    private BigDecimal price;
    private BigDecimal discountPrice; // Giá sau khi giảm
    private Integer durationDays; // Số ngày sử dụng
    private String durationUnit; // days, months, years
    private Integer durationValue; // VD: 1 month, 3 months, 1 year
    
    // Quyền lợi
    private Boolean unlimitedAccess; // Truy cập không giới hạn
    private Integer gymAccessCount; // Số lần vào phòng gym (nếu có giới hạn)
    private Boolean personalTraining; // Có personal training không
    private Integer personalTrainingSessionsIncluded; // Số buổi PT được bao gồm
    private Boolean groupClassesIncluded; // Được tham gia lớp nhóm
    private Integer groupClassesCount; // Số lớp nhóm/tháng
    private Boolean saunaAccess; // Phòng xông hơi
    private Boolean lockerAccess; // Tủ khóa
    private Boolean swimmingPoolAccess; // Bể bơi
    private Boolean nutritionConsultation; // Tư vấn dinh dưỡng
    private Boolean freeWifi;
    private Boolean freeDrinks; // Đồ uống miễn phí
    private Boolean freeParking; // Đỗ xe miễn phí
    private Boolean guestPass; // Cho phép mang khách
    private Integer guestPassCount; // Số lần mang khách
    
    // Giờ sử dụng
    private String accessHours; // all_day, morning_only, evening_only, off_peak
    private String accessDays; // all_week, weekdays_only, weekends_only
    
    // Ưu đãi và khuyến mãi
    private Boolean isPromotional; // Gói khuyến mãi
    private Date promotionStartDate;
    private Date promotionEndDate;
    private BigDecimal registrationFee; // Phí đăng ký ban đầu
    private Boolean waiveRegistrationFee; // Miễn phí đăng ký
    
    // Điều khoản
    private Boolean autoRenewal; // Tự động gia hạn
    private Boolean refundable; // Có thể hoàn tiền
    private Integer cancellationNoticeDays; // Số ngày báo trước khi hủy
    private String termsAndConditions;
    
    // Giới hạn và yêu cầu
    private Integer minAge; // Tuổi tối thiểu
    private Integer maxAge; // Tuổi tối đa
    private Integer maxMembers; // Số lượng thành viên tối đa (cho gói gia đình)
    private Boolean requiresMedicalCertificate; // Yêu cầu giấy khám sức khỏe
    
    // Thống kê
    private Integer totalSubscribers; // Tổng số người đăng ký
    private Integer activeSubscribers; // Số người đang sử dụng
    private BigDecimal totalRevenue; // Tổng doanh thu
    private Double averageRating; // Đánh giá trung bình
    
    // Trạng thái
    private String status; // active, inactive, discontinued
    private Boolean isVisible; // Hiển thị trên website
    private Boolean isFeatured; // Gói nổi bật
    private Integer displayOrder; // Thứ tự hiển thị
    
    // Icon và màu sắc (cho UI)
    private String iconUrl;
    private String colorCode; // Mã màu HEX
    private String badgeText; // VD: "Most Popular", "Best Value"
    
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    // Constructors
    public MembershipPackage() {
        this.status = "active";
        this.isVisible = true;
        this.isFeatured = false;
        this.isPromotional = false;
        this.totalSubscribers = 0;
        this.activeSubscribers = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.averageRating = 0.0;
        this.unlimitedAccess = false;
        this.autoRenewal = false;
        this.refundable = false;
    }

    public MembershipPackage(String packageCode, String packageName, BigDecimal price, Integer durationDays) {
        this();
        this.packageCode = packageCode;
        this.packageName = packageName;
        this.price = price;
        this.durationDays = durationDays;
    }

    // Getters and Setters
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Integer getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(Integer durationValue) {
        this.durationValue = durationValue;
    }

    public Boolean getUnlimitedAccess() {
        return unlimitedAccess;
    }

    public void setUnlimitedAccess(Boolean unlimitedAccess) {
        this.unlimitedAccess = unlimitedAccess;
    }

    public Integer getGymAccessCount() {
        return gymAccessCount;
    }

    public void setGymAccessCount(Integer gymAccessCount) {
        this.gymAccessCount = gymAccessCount;
    }

    public Boolean getPersonalTraining() {
        return personalTraining;
    }

    public void setPersonalTraining(Boolean personalTraining) {
        this.personalTraining = personalTraining;
    }

    public Integer getPersonalTrainingSessionsIncluded() {
        return personalTrainingSessionsIncluded;
    }

    public void setPersonalTrainingSessionsIncluded(Integer personalTrainingSessionsIncluded) {
        this.personalTrainingSessionsIncluded = personalTrainingSessionsIncluded;
    }

    public Boolean getGroupClassesIncluded() {
        return groupClassesIncluded;
    }

    public void setGroupClassesIncluded(Boolean groupClassesIncluded) {
        this.groupClassesIncluded = groupClassesIncluded;
    }

    public Integer getGroupClassesCount() {
        return groupClassesCount;
    }

    public void setGroupClassesCount(Integer groupClassesCount) {
        this.groupClassesCount = groupClassesCount;
    }

    public Boolean getSaunaAccess() {
        return saunaAccess;
    }

    public void setSaunaAccess(Boolean saunaAccess) {
        this.saunaAccess = saunaAccess;
    }

    public Boolean getLockerAccess() {
        return lockerAccess;
    }

    public void setLockerAccess(Boolean lockerAccess) {
        this.lockerAccess = lockerAccess;
    }

    public Boolean getSwimmingPoolAccess() {
        return swimmingPoolAccess;
    }

    public void setSwimmingPoolAccess(Boolean swimmingPoolAccess) {
        this.swimmingPoolAccess = swimmingPoolAccess;
    }

    public Boolean getNutritionConsultation() {
        return nutritionConsultation;
    }

    public void setNutritionConsultation(Boolean nutritionConsultation) {
        this.nutritionConsultation = nutritionConsultation;
    }

    public Boolean getFreeWifi() {
        return freeWifi;
    }

    public void setFreeWifi(Boolean freeWifi) {
        this.freeWifi = freeWifi;
    }

    public Boolean getFreeDrinks() {
        return freeDrinks;
    }

    public void setFreeDrinks(Boolean freeDrinks) {
        this.freeDrinks = freeDrinks;
    }

    public Boolean getFreeParking() {
        return freeParking;
    }

    public void setFreeParking(Boolean freeParking) {
        this.freeParking = freeParking;
    }

    public Boolean getGuestPass() {
        return guestPass;
    }

    public void setGuestPass(Boolean guestPass) {
        this.guestPass = guestPass;
    }

    public Integer getGuestPassCount() {
        return guestPassCount;
    }

    public void setGuestPassCount(Integer guestPassCount) {
        this.guestPassCount = guestPassCount;
    }

    public String getAccessHours() {
        return accessHours;
    }

    public void setAccessHours(String accessHours) {
        this.accessHours = accessHours;
    }

    public String getAccessDays() {
        return accessDays;
    }

    public void setAccessDays(String accessDays) {
        this.accessDays = accessDays;
    }

    public Boolean getIsPromotional() {
        return isPromotional;
    }

    public void setIsPromotional(Boolean isPromotional) {
        this.isPromotional = isPromotional;
    }

    public Date getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(Date promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public Date getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(Date promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public BigDecimal getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Boolean getWaiveRegistrationFee() {
        return waiveRegistrationFee;
    }

    public void setWaiveRegistrationFee(Boolean waiveRegistrationFee) {
        this.waiveRegistrationFee = waiveRegistrationFee;
    }

    public Boolean getAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(Boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }

    public Integer getCancellationNoticeDays() {
        return cancellationNoticeDays;
    }

    public void setCancellationNoticeDays(Integer cancellationNoticeDays) {
        this.cancellationNoticeDays = cancellationNoticeDays;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Boolean getRequiresMedicalCertificate() {
        return requiresMedicalCertificate;
    }

    public void setRequiresMedicalCertificate(Boolean requiresMedicalCertificate) {
        this.requiresMedicalCertificate = requiresMedicalCertificate;
    }

    public Integer getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(Integer totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }

    public Integer getActiveSubscribers() {
        return activeSubscribers;
    }

    public void setActiveSubscribers(Integer activeSubscribers) {
        this.activeSubscribers = activeSubscribers;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
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
    public BigDecimal getEffectivePrice() {
        return discountPrice != null ? discountPrice : price;
    }

    public BigDecimal getDiscountPercentage() {
        if (discountPrice == null || price == null || price.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return price.subtract(discountPrice)
                    .divide(price, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
    }

    public boolean isPromotionActive() {
        if (!isPromotional || promotionStartDate == null || promotionEndDate == null) {
            return false;
        }
        Date now = new Date();
        return now.after(promotionStartDate) && now.before(promotionEndDate);
    }

    public BigDecimal getPricePerDay() {
        if (durationDays == null || durationDays == 0) return BigDecimal.ZERO;
        return getEffectivePrice().divide(new BigDecimal(durationDays), 2, BigDecimal.ROUND_HALF_UP);
    }

    public void addSubscriber() {
        this.totalSubscribers++;
        this.activeSubscribers++;
    }

    public void removeSubscriber() {
        if (this.activeSubscribers > 0) {
            this.activeSubscribers--;
        }
    }

    @Override
    public String toString() {
        return "MembershipPackage{" +
                "packageId=" + packageId +
                ", packageCode='" + packageCode + '\'' +
                ", packageName='" + packageName + '\'' +
                ", price=" + price +
                ", durationDays=" + durationDays +
                ", status='" + status + '\'' +
                '}';
    }
}

