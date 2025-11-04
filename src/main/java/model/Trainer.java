
package model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

/*
    Note: 
 */
@Entity
@Table(name = "trainer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainer extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "hiredDate")
    @Temporal(TemporalType.DATE)
    private Date hiredDate;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "salary")
    private Double salary;
    @Column(name = "star")
    private Double star;

    public Trainer() {
    }

    public Trainer(Integer userId) {
        super(userId);
    }

    public Date getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
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
        if (!(object instanceof Trainer)) {
            return false;
        }
        Trainer other = (Trainer) object;
        if ((this.getUserId() == null && other.getUserId() != null)
                || (this.getUserId() != null && !this.getUserId().equals(other.getUserId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Trainer{id=" + this.getUserId() + ", hiredDate=" + hiredDate + ", salary=" + salary + ", star=" + star
                + '}';
    }

}
