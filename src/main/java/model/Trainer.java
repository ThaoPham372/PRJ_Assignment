package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "trainer")
public class Trainer extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "certification_level")
    private String certificationLevel;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "workAt")
    private String workAt;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        setRole("Trainer");
    }

    @PreUpdate
    @Override
    protected void onUpdate() {
        super.onUpdate();
        setRole("Trainer");
    }

    public Trainer() {
    }

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

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getWorkAt() {
        return workAt;
    }

    public void setWorkAt(String workAt) {
        this.workAt = workAt;
    }

    @Override
    public String toString() {
        return "Trainer{" + "specialization=" + specialization + ", salary=" + salary + '}';
    }
}
