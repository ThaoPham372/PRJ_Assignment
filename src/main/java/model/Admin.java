package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends User {

    @Column(name = "note")
    private String note;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        setRole("Admin");
    }

    @PreUpdate
    @Override
    protected void onUpdate() {
        super.onUpdate();
        setRole("Admin");
    }

    public Admin() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Admin{id=" + getId() + ", note=" + note + '}';
    }
}
