package com.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

/**
 *
 * @author Bert
 */
@Entity
@Table(name="settings")
public class Settings implements java.io.Serializable {
    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="system_loss", precision=7, nullable=false)
    private Double systemLoss;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="depreciation_fund",precision=7, nullable=false)
    private Double depreciationFund;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="pes", nullable=false, precision=7)
    private Double pes;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="penalty", nullable=false, precision=7)
    private Double penalty;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="basic", nullable=false, precision=7)
    private Double basic;
    @NotNull @Max(value = 5, message = "Must be lesser than 5.")
    @Min(value=1, message="Must be greater than zero") @Digits(integer=2,fraction = 0, message="Must be a number")
    @Column(name="debts_allowed", nullable = false)
    private Integer debtsAllowed;

    public Settings() { }

    public Settings(Double systemLoss, Double depreciationFund, Double pes, Double penalty, Double basic) {
        this.systemLoss = systemLoss;
        this.depreciationFund = depreciationFund;
        this.pes = pes;
        this.penalty = penalty;
        this.basic = basic;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSystemLoss() {
        return systemLoss;
    }

    public void setSystemLoss(Double systemLoss) {
        this.systemLoss = systemLoss;
    }

    public Double getDepreciationFund() {
        return depreciationFund;
    }

    public void setDepreciationFund(Double depreciationFund) {
        this.depreciationFund = depreciationFund;
    }

    public Double getPes() {
        return pes;
    }

    public void setPes(Double pes) {
        this.pes = pes;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }  

    public Double getBasic() {
        return basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Integer getDebtsAllowed() {
        return debtsAllowed;
    }

    public void setDebtsAllowed(Integer debtsAllowed) {
        this.debtsAllowed = debtsAllowed;
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
        final Settings other = (Settings) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
}
