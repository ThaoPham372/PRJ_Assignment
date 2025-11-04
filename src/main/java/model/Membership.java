
package model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

/*
    Note: 
 */
@Entity
@Table(name = "memberships")
public class Membership implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "membership_id")
    private int membershipId;
    
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId; 
    
    @Basic(optional = false)
    @Column(name = "package_id")
    private int packageId;
    
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "status")
    private String status;
    
    @Lob
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Transient
    private String name;
    @Transient
    private String email;
    @Transient
    private String phone;
    @Transient
    private String packageType;

    public Membership() {
        createdDate = updatedDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        status = "ACTIVE";
    }

    public Membership(int membershipId) {
        this.membershipId = membershipId;
    }

    public Membership(int membershipId, Date startDate, Date endDate) {
        this.membershipId = membershipId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Membership{" + "membershipId=" + membershipId + ", userId=" + userId + ", packageId=" + packageId + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + ", createdDate=" + createdDate + ", name=" + name + ", email=" + email + ", phone=" + phone + ", packageType=" + packageType + '}';
    }
}
