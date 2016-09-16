package com.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by jasper on 7/26/16.
 */
@Entity
@Table(name="expense")
public class Expense extends AuditableEntity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @Column(name="type", nullable=false)
    private Integer type;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="schedule_id", nullable=false)
    private Schedule schedule;
    @Digits(fraction=2, integer=10, message = "Invalid amount") @NotNull(message="Invalid input") @Min(value=1, message = "Invalid amount")
    @Column(name="amount", nullable=false, precision=9)
    private BigDecimal amount;

    public Expense() {}

    public Expense(Integer type, Schedule schedule, BigDecimal amount) {
        this.type = type;
        this.schedule = schedule;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    @JsonManagedReference
    public Schedule getSchedule() {
        return schedule;
    }
    @JsonProperty
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        final Expense other = (Expense) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
}
