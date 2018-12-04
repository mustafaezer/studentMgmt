/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Turktrust Stajyer
 */
@Entity
@Table(name = "announcementinfo", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Announcementinfo.findAll", query = "SELECT a FROM Announcementinfo a")
    , @NamedQuery(name = "Announcementinfo.findByAnnouncementId", query = "SELECT a FROM Announcementinfo a WHERE a.announcementId = :announcementId")
    , @NamedQuery(name = "Announcementinfo.findByType", query = "SELECT a FROM Announcementinfo a WHERE a.type = :type")
    , @NamedQuery(name = "Announcementinfo.findByImportance", query = "SELECT a FROM Announcementinfo a WHERE a.importance = :importance")
    , @NamedQuery(name = "Announcementinfo.findByHeader", query = "SELECT a FROM Announcementinfo a WHERE a.header = :header")
    , @NamedQuery(name = "Announcementinfo.findByContext", query = "SELECT a FROM Announcementinfo a WHERE a.context = :context")
    , @NamedQuery(name = "Announcementinfo.findByStartDate", query = "SELECT a FROM Announcementinfo a WHERE a.startDate = :startDate")
    , @NamedQuery(name = "Announcementinfo.findByEndDate", query = "SELECT a FROM Announcementinfo a WHERE a.endDate = :endDate")
    , @NamedQuery(name = "Announcementinfo.findByIsActive", query = "SELECT a FROM Announcementinfo a WHERE a.isActive = :isActive")})
public class Announcementinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "announcementId")
    private Integer announcementId;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "importance")
    private String importance;
    @Basic(optional = false)
    @Column(name = "header")
    private String header;
    @Basic(optional = false)
    @Column(name = "context")
    private String context;
    @Basic(optional = false)
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "isActive")
    private String isActive;
    @JoinColumn(name = "departmentInfoId", referencedColumnName = "departmentInfoId")
    @ManyToOne(optional = false)
    private Departmentinfo departmentInfoId;
    @JoinColumn(name = "authorCitizenshipNumber", referencedColumnName = "citizenshipNumber")
    @ManyToOne(optional = false)
    private Userinfo authorCitizenshipNumber;

    public Announcementinfo() {
    }

    public Announcementinfo(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public Announcementinfo(Integer announcementId, String type, String importance, String header, String context, Date startDate, String isActive) {
        this.announcementId = announcementId;
        this.type = type;
        this.importance = importance;
        this.header = header;
        this.context = context;
        this.startDate = startDate;
        this.isActive = isActive;
    }

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Departmentinfo getDepartmentInfoId() {
        return departmentInfoId;
    }

    public void setDepartmentInfoId(Departmentinfo departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
    }

    public Userinfo getAuthorCitizenshipNumber() {
        return authorCitizenshipNumber;
    }

    public void setAuthorCitizenshipNumber(Userinfo authorCitizenshipNumber) {
        this.authorCitizenshipNumber = authorCitizenshipNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (announcementId != null ? announcementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Announcementinfo)) {
            return false;
        }
        Announcementinfo other = (Announcementinfo) object;
        if ((this.announcementId == null && other.announcementId != null) || (this.announcementId != null && !this.announcementId.equals(other.announcementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Announcementinfo[ announcementId=" + announcementId + " ]";
    }
    
}
