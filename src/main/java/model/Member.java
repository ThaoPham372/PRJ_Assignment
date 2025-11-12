package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "members")
@PrimaryKeyJoinColumn(name = "member_id", referencedColumnName = "user_id")
public class Member extends User {

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
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

    @Column(name = "goal")
    private String goal;

    @Lob
    @Column(name = "pt_note")
    private String ptNote;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private Collection<Membership> membershipCollection;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        setRole("Member");
    }

    @PreUpdate
    @Override
    protected void onUpdate() {
        super.onUpdate();
        setRole("Member");
    }

    public Member() {
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPtNote() {
        return ptNote;
    }

    public void setPtNote(String ptNote) {
        this.ptNote = ptNote;
    }

    public Collection<Membership> getMembershipCollection() {
        return membershipCollection;
    }

    public void setMembershipCollection(Collection<Membership> membershipCollection) {
        this.membershipCollection = membershipCollection;
    }

    @Override
    public String toString() {
        return "Member{id=" + getId() + ", username=" + getUsername() + ", weight=" + weight + ", height=" + height
                + ", bmi=" + bmi + ", goal=" + goal + '}';
    }
}
