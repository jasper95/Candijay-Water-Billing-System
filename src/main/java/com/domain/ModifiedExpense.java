package com.domain;

import com.dao.util.AuditorDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by jasper on 8/13/16.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="modified_expense")
public class ModifiedExpense implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="expense_id", nullable=false)
    private Expense expense;
    @Column(name="type", nullable=false)
    private Integer type;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="schedule_id", nullable=false)
    private Schedule schedule;
    @Digits(fraction=2, integer=10, message = "Invalid amount") @NotNull(message="Invalid input") @Min(value=1, message = "Invalid amount")
    @Column(name="amount", nullable=false, precision=9)
    private BigDecimal amount;
    @JsonSerialize(using=AuditorDateTimeSerializer.class)
    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime creationTime;
    @Column(name = "created_by_user", nullable = false)
    @CreatedBy
    private String createdByUser;

    public ModifiedExpense() {}

    public ModifiedExpense(Expense expense){
        this.type = expense.getType();
        this.schedule = expense.getSchedule();
        this.amount = expense.getAmount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
