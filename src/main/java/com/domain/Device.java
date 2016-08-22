package com.domain;
// Generated Apr 16, 2015 12:48:29 PM by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Device generated by hbm2java
 */
@Entity
@Table(name="device"
    ,catalog="revised_cws_db",
     uniqueConstraints = @UniqueConstraint(columnNames="meter_code") 
)
public class Device  implements java.io.Serializable {
    
    @Id @GeneratedValue(strategy=IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="owner_id", nullable=false)
    private Account owner;
    @NotEmpty(message="Invalid meter code") @NotBlank(message="Invalid meter code")
    @Column(name="meter_code", unique=true, length=50, nullable=false)
    private String meterCode;
    @NotBlank(message="Invalid meter brand") @NotEmpty(message="Invalid meter brand")
    @Column(name="brand", length=45, nullable=false)
    private String brand;
    @Column(name="start_date",length=10)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name="end_date", length=10)
    private Date endDate;
    @Column(name="active", nullable=false)
    private boolean active = false;
    @Column(name="last_reading", nullable=false)
    private Integer lastReading = 0;
    
    public Device() {
    }

    public Device(Account owner, Date startDate, String meterCode, String brand) {
        this.meterCode = meterCode;
        this.startDate = startDate;
        this.brand = brand;
        this.owner = owner;
    }
    public Device(Account owner, Date startDate, Date endDate, String meterCode, String brand) {
       this.startDate = startDate;
       this.endDate = endDate;
       this.meterCode = meterCode;
       this.brand = brand;
       this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMeterCode() {
        return this.meterCode;
    }
    
    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getLastReading() {
        return lastReading;
    }

    public void setLastReading(Integer lastReading) {
        this.lastReading = lastReading;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                        append(this.id).
                        toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
    
}