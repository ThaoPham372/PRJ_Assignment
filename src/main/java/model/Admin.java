package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "admin")
public class Admin extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "note")
    private String note;

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
