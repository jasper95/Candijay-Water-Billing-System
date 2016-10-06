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
    @Column(name="system_loss_rate", precision=7, nullable=false)
    private Double systemLossRate;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="depreciation_fund_rate",precision=7, nullable=false)
    private Double depreciationFundRate;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="pes", nullable=false, precision=7)
    private Double pes;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="penalty", nullable=false, precision=7)
    private Double penalty;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="basic_rate", nullable=false, precision=7)
    private Double basicRate;
    @NotNull @Max(value = 5, message = "Must be lesser than 5.")
    @Min(value=1, message="Must be greater than zero") @Digits(integer=2,fraction = 0, message="Must be a number")
    @Column(name="debts_allowed", nullable = false)
    private Integer debtsAllowed;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="min_basic", nullable = false)
    private Double basicMinimum;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="min_depreciation_fund", nullable = false)
    private Double depreciationFundMinimum;
    @NotNull @DecimalMin(value="0.01", message="Must be greater than zero")
    @Column(name="min_system_loss", nullable = false)
    private Double systemLossMinimum;


    public Settings() { }

    public Settings(Double systemLossRate, Double depreciationFundRate, Double pes, Double penalty, Double basicRate, Integer debtsAllowed, Double basicMinimum, Double depreciationFundMinimum, Double systemLossMinimum) {
        this.systemLossRate = systemLossRate;
        this.depreciationFundRate = depreciationFundRate;
        this.pes = pes;
        this.penalty = penalty;
        this.basicRate = basicRate;
        this.debtsAllowed = debtsAllowed;
        this.basicMinimum = basicMinimum;
        this.depreciationFundMinimum = depreciationFundMinimum;
        this.systemLossMinimum = systemLossMinimum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSystemLossRate() {
        return systemLossRate;
    }

    public void setSystemLossRate(Double systemLossRate) {
        this.systemLossRate = systemLossRate;
    }

    public Double getDepreciationFundRate() {
        return depreciationFundRate;
    }

    public void setDepreciationFundRate(Double depreciationFundRate) {
        this.depreciationFundRate = depreciationFundRate;
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

    public Double getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(Double basic) {
        this.basicRate = basic;
    }

    public Integer getDebtsAllowed() {
        return debtsAllowed;
    }

    public void setDebtsAllowed(Integer debtsAllowed) {
        this.debtsAllowed = debtsAllowed;
    }

    public Double getBasicMinimum() {
        return basicMinimum;
    }

    public void setBasicMinimum(Double basicMinimum) {
        this.basicMinimum = basicMinimum;
    }

    public Double getDepreciationFundMinimum() {
        return depreciationFundMinimum;
    }

    public void setDepreciationFundMinimum(Double depreciationFundMinimum) {
        this.depreciationFundMinimum = depreciationFundMinimum;
    }

    public Double getSystemLossMinimum() {
        return systemLossMinimum;
    }

    public void setSystemLossMinimum(Double systemLossMinimum) {
        this.systemLossMinimum = systemLossMinimum;
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
