package com.gym.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * Admin entity representing an admin user.
 * Extends User using JOINED inheritance strategy.
 */
@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "note")
    private String note;

    public Admin() {
        super();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getUserId() != null ? this.getUserId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.getUserId() == null && other.getUserId() != null) || (this.getUserId() != null && !this.getUserId().equals(other.getUserId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Admin{" + "id=" + this.getUserId() + ", note=" + note + '}';
    }

    
}
