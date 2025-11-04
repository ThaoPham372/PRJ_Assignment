
package model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/*
    Note: 
 */
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "weight")
    private Float weight;
    @Column(name = "height")
    private Float height;
    @Column(name = "bmi")
    private Float bmi;
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;
    @Column(name = "emergency_contact_relation")
    private String emergencyContactRelation;
    @Column(name = "emergency_contact_address")
    private String emergencyContactAddress;

    public Student() {
    }

    public Student(Integer userId) {
        super(userId);
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
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

    public String getEmergencyContactAddress() {
        return emergencyContactAddress;
    }

    public void setEmergencyContactAddress(String emergencyContactAddress) {
        this.emergencyContactAddress = emergencyContactAddress;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getUserId() != null ? getUserId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((getUserId() == null && getUserId() != null) || (getUserId() != null && !this.getUserId().equals(getUserId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Student[ userid=" + getUserId() + ", height="+ height +"]";
    }

}
