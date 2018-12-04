/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Turktrust Stajyer
 */
@Entity
@Table(name = "userinfo", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userinfo.findAll", query = "SELECT u FROM Userinfo u")
    , @NamedQuery(name = "Userinfo.findByCitizenshipNumber", query = "SELECT u FROM Userinfo u WHERE u.citizenshipNumber = :citizenshipNumber")
    , @NamedQuery(name = "Userinfo.findByUserType", query = "SELECT u FROM Userinfo u WHERE u.userType = :userType")
    , @NamedQuery(name = "Userinfo.findByFirstName", query = "SELECT u FROM Userinfo u WHERE u.firstName = :firstName")
    , @NamedQuery(name = "Userinfo.findByMiddleName", query = "SELECT u FROM Userinfo u WHERE u.middleName = :middleName")
    , @NamedQuery(name = "Userinfo.findByLastName", query = "SELECT u FROM Userinfo u WHERE u.lastName = :lastName")
    , @NamedQuery(name = "Userinfo.findByGender", query = "SELECT u FROM Userinfo u WHERE u.gender = :gender")
    , @NamedQuery(name = "Userinfo.findByEmail", query = "SELECT u FROM Userinfo u WHERE u.email = :email")
    , @NamedQuery(name = "Userinfo.findByPassword", query = "SELECT u FROM Userinfo u WHERE u.password = :password")
    , @NamedQuery(name = "Userinfo.findByDateOfBirth", query = "SELECT u FROM Userinfo u WHERE u.dateOfBirth = :dateOfBirth")
    , @NamedQuery(name = "Userinfo.findByRemark", query = "SELECT u FROM Userinfo u WHERE u.remark = :remark")
    , @NamedQuery(name = "Userinfo.findByIsActive", query = "SELECT u FROM Userinfo u WHERE u.isActive = :isActive")})
public class Userinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "citizenshipNumber")
    private String citizenshipNumber;
    @Basic(optional = false)
    @Column(name = "userType")
    private String userType;
    @Basic(optional = false)
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "middleName")
    private String middleName;
    @Basic(optional = false)
    @Column(name = "lastName")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "gender")
    private String gender;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "dateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Basic(optional = false)
    @Column(name = "remark")
    private String remark;
    @Basic(optional = false)
    @Column(name = "isActive")
    private String isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userinfo")
    private List<Grading> gradingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authorCitizenshipNumber")
    private List<Announcementinfo> announcementinfoList;
    @JoinColumn(name = "departmentInfoId", referencedColumnName = "departmentInfoId")
    @ManyToOne(optional = false)
    private Departmentinfo departmentInfoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instructorCitizenshipNumber")
    private List<Subjectinfo> subjectinfoList;

    public Userinfo() {
    }

    public Userinfo(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public Userinfo(String citizenshipNumber, String userType, String firstName, String lastName, String gender, String email, String password, Date dateOfBirth, String remark, String isActive) {
        this.citizenshipNumber = citizenshipNumber;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.remark = remark;
        this.isActive = isActive;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public List<Grading> getGradingList() {
        return gradingList;
    }

    public void setGradingList(List<Grading> gradingList) {
        this.gradingList = gradingList;
    }

    @XmlTransient
    public List<Announcementinfo> getAnnouncementinfoList() {
        return announcementinfoList;
    }

    public void setAnnouncementinfoList(List<Announcementinfo> announcementinfoList) {
        this.announcementinfoList = announcementinfoList;
    }

    public Departmentinfo getDepartmentInfoId() {
        return departmentInfoId;
    }

    public void setDepartmentInfoId(Departmentinfo departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
    }

    @XmlTransient
    public List<Subjectinfo> getSubjectinfoList() {
        return subjectinfoList;
    }

    public void setSubjectinfoList(List<Subjectinfo> subjectinfoList) {
        this.subjectinfoList = subjectinfoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citizenshipNumber != null ? citizenshipNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userinfo)) {
            return false;
        }
        Userinfo other = (Userinfo) object;
        if ((this.citizenshipNumber == null && other.citizenshipNumber != null) || (this.citizenshipNumber != null && !this.citizenshipNumber.equals(other.citizenshipNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Userinfo[ citizenshipNumber=" + citizenshipNumber + " ]";
    }
    
}
