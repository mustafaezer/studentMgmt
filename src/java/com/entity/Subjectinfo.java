/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Turktrust Stajyer
 */
@Entity
@Table(name = "subjectinfo", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subjectinfo.findAll", query = "SELECT s FROM Subjectinfo s")
    , @NamedQuery(name = "Subjectinfo.findBySubjectInfoId", query = "SELECT s FROM Subjectinfo s WHERE s.subjectInfoId = :subjectInfoId")
    , @NamedQuery(name = "Subjectinfo.findByName", query = "SELECT s FROM Subjectinfo s WHERE s.name = :name")
    , @NamedQuery(name = "Subjectinfo.findByQuota", query = "SELECT s FROM Subjectinfo s WHERE s.quota = :quota")
    , @NamedQuery(name = "Subjectinfo.findByYear", query = "SELECT s FROM Subjectinfo s WHERE s.year = :year")
    , @NamedQuery(name = "Subjectinfo.findBySeason", query = "SELECT s FROM Subjectinfo s WHERE s.season = :season")
    , @NamedQuery(name = "Subjectinfo.findByIsActive", query = "SELECT s FROM Subjectinfo s WHERE s.isActive = :isActive")})
public class Subjectinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "subjectInfoId")
    private Integer subjectInfoId;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "quota")
    private int quota;

    @Basic(optional = false)
    @Column(name = "year")
    private String year;

    @Basic(optional = false)
    @Column(name = "season")
    private String season;

    @Basic(optional = false)
    @Column(name = "isActive")
    private String isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectinfo")
    private List<Grading> gradingList;

    @JoinColumn(name = "departmentInfoId", referencedColumnName = "departmentInfoId")
    @ManyToOne(optional = false)
    private Departmentinfo departmentInfoId;

    @JoinColumn(name = "instructorCitizenshipNumber", referencedColumnName = "citizenshipNumber")
    @ManyToOne(optional = false)
    private Userinfo instructorCitizenshipNumber;

    public Subjectinfo() {
    }

    public Subjectinfo(Integer subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public Subjectinfo(Integer subjectInfoId, String name, int quota, String year, String season, String isActive) {
        this.subjectInfoId = subjectInfoId;
        this.name = name;
        this.quota = quota;
        this.year = year;
        this.season = season;
        this.isActive = isActive;
    }

    public Integer getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(Integer subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
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

    public Departmentinfo getDepartmentInfoId() {
        return departmentInfoId;
    }

    public void setDepartmentInfoId(Departmentinfo departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
    }

    public Userinfo getInstructorCitizenshipNumber() {
        return instructorCitizenshipNumber;
    }

    public void setInstructorCitizenshipNumber(Userinfo instructorCitizenshipNumber) {
        this.instructorCitizenshipNumber = instructorCitizenshipNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subjectInfoId != null ? subjectInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subjectinfo)) {
            return false;
        }
        Subjectinfo other = (Subjectinfo) object;
        if ((this.subjectInfoId == null && other.subjectInfoId != null) || (this.subjectInfoId != null && !this.subjectInfoId.equals(other.subjectInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Subjectinfo[ subjectInfoId=" + subjectInfoId + " ]";
    }

}
