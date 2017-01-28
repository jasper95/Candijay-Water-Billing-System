package com.domain;
// Generated Apr 16, 2015 12:48:29 PM by Hibernate Tools 4.3.1


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Customer generated by hbm2java
 */
@Entity
@Table(name="customer")
public class Customer  implements java.io.Serializable {
    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @NotBlank @NotEmpty
    @Column(name="first_name", nullable=false, length=45)
    private String firstName;
    @NotBlank @NotEmpty
    @Column(name="lastname", nullable=false, length=45)
    private String lastname;
    @NotBlank @NotEmpty
    @Column(name="middle_name", nullable=false, length=45)
    private String middleName;
    @NotEmpty
    @Pattern(regexp = "^[M|F]{1}$", message ="This field is required")
    @Column(name="gender", nullable=false, length=1)
    private String gender;
    @NotEmpty
    @Digits(fraction = 0, integer = 11, message="Invalid format")
    @Column(name="contact_number")
    private String contactNumber;
    @NotEmpty @Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="Invalid number")
    @Column(name="family_members_count", nullable=false)
    private String familyMembersCount;
    @Column(name="occupation", nullable=false, length=45)
    private String occupation;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="customer", cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Account> accounts = new HashSet<Account>(0);
    
    public Customer() { }

	
    public Customer(Long id, String firstName, String lastname, String middleName, String gender, String familyMembersCount, String occupation, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.middleName = middleName;
        this.gender = gender;
        this.familyMembersCount = familyMembersCount;
        this.occupation = occupation;
    }
    public Customer(Long id, String firstName, String middleName, String gender, String contactNumber, String familyMembersCount, String occupation, Set<Account> accounts) {
       this.id = id;
       this.firstName = firstName;
       this.middleName = middleName;
       this.gender = gender;
       this.contactNumber = contactNumber;
       this.familyMembersCount = familyMembersCount;
       this.occupation = occupation;
       this.accounts = accounts;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return this.gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFamilyMembersCount() {
        return this.familyMembersCount;
    }
    
    public void setFamilyMembersCount(String familyMembersCount) {
        this.familyMembersCount = familyMembersCount;
    }

    public String getOccupation() {
        return this.occupation;
    }
    
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName(){
        return this.firstName+" "+this.lastname;
    }

    @JsonBackReference
    public Set<Account> getAccounts() {
        return this.accounts;
    }
    @JsonProperty
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
    
    
    
    @Override
    public String toString() {
        return this.firstName+" "+this.middleName+" "+this.lastname;
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
        final Customer other = (Customer) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
}


