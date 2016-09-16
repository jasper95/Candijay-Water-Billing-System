package com.domain;

import com.dao.util.AuditorDateTimeSerializer;
import com.dao.util.StandardDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by jasper on 8/3/16.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="modified_payment")
public class ModifiedPayment implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="payment_id", nullable=false)
    private Payment payment;
    @JsonSerialize(using= StandardDateTimeSerializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name="date", nullable=false, length=10)
    private DateTime date;
    @Column(name="amount_paid", nullable=false, precision=10, scale=0)
    private BigDecimal amountPaid;
    @Column(name="discount", nullable=false, precision=10, scale=0)
    private BigDecimal discount;
    @Column(name="or_number", nullable = false)
    private String receiptNumber;
    @JsonSerialize(using=AuditorDateTimeSerializer.class)
    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime creationTime;
    @Column(name = "created_by_user", nullable = false)
    @CreatedBy
    private String createdByUser;

    public ModifiedPayment() { }

    public ModifiedPayment(Payment payment) {
        this.amountPaid = payment.getAmountPaid();
        this.date = payment.getDate();
        this.discount = payment.getDiscount();
        this.receiptNumber = payment.getReceiptNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonManagedReference
    public Payment getPayment() {
        return payment;
    }
    @JsonProperty
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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
        final ModifiedPayment other = (ModifiedPayment) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
}
