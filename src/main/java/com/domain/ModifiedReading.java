package com.domain;

import com.dao.util.AuditorDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by jasper on 8/14/16.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="modified_reading")
public class ModifiedReading implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="reading_id", nullable=false)
    private MeterReading reading;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="schedule_id", nullable=false)
    private Schedule schedule;
    @Digits(fraction=0, integer=10) @NotNull
    @Column(name="reading_value", nullable=false)
    private Integer readingValue;
    @Column(name="consumption", nullable=false)
    private Integer consumption;
    @JsonSerialize(using=AuditorDateTimeSerializer.class)
    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime creationTime;
    @Column(name = "created_by_user", nullable = false)
    @CreatedBy
    private String createdByUser;

    public ModifiedReading() {}

    public ModifiedReading(MeterReading reading){
        this.schedule = reading.getSchedule();
        this.consumption = reading.getConsumption();
        this.readingValue = reading.getReadingValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonManagedReference
    public MeterReading getReading() {
        return reading;
    }

    @JsonProperty
    public void setReading(MeterReading reading) {
        this.reading = reading;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Integer getReadingValue() {
        return readingValue;
    }

    public void setReadingValue(Integer readingValue) {
        this.readingValue = readingValue;
    }

    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }
}
