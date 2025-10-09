package model;

import java.util.Date;

/**
 * Coach model class representing a trainer/coach in the gym
 */
public class Coach {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String specialization; // bodybuilding, yoga, strength, etc.
    private int experienceYears;
    private double salary;
    private String certifications;
    private String description;
    private String profileImage;
    private Date joinDate;
    private String status; // active, inactive, busy
    private String notes;
    private int rating;
    private int totalClients;
    private int sessionsPerWeek;

    // Default constructor
    public Coach() {
    }

    // Constructor with basic info
    public Coach(String fullName, String email, String phone, String specialization) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.joinDate = new Date();
        this.status = "active";
        this.rating = 5;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    public void setSessionsPerWeek(int sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

