package com.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
/**
 *
 * @author Bert
 */
@Entity
@Table(name="settings"
    ,catalog="revised_cws_db"
)
public class Settings implements java.io.Serializable {
    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @NotNull @DecimalMin(value="0.01", message="System loss must be greater than zero")
    @Column(name="system_loss", precision=7, nullable=false)
    private Double systemLoss;
    @NotNull @DecimalMin(value="0.01", message="Dep fund must be greater than zero")
    @Column(name="depreciation_fund",precision=7, nullable=false)
    private Double depreciationFund;
    @NotNull @DecimalMin(value="0.01", message="PES must be greater than zero")
    @Column(name="pes", nullable=false, precision=7)
    private Double pes;
    @NotNull @DecimalMin(value="0.01", message="Penalty must be greater than zero")
    @Column(name="penalty", nullable=false, precision=7)
    private Double penalty;
    @NotNull @DecimalMin(value="0.01", message="Basic must be greater than zero")
    @Column(name="basic", nullable=false, precision=7)
    private Double basic;
    
    public Settings() { }

    public Settings(Long id, Double systemLoss, Double depreciationFund, Double pes, Double penalty, Double basic) {
        this.id = id;
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
}
