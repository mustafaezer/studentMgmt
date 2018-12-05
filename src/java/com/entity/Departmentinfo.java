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
@Table(name = "departmentinfo", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departmentinfo.findAll", query = "SELECT d FROM Departmentinfo d")
    , @NamedQuery(name = "Departmentinfo.findByDepartmentInfoId", query = "SELECT d FROM Departmentinfo d WHERE d.departmentInfoId = :departmentInfoId")
    , @NamedQuery(name = "Departmentinfo.findByName", query = "SELECT d FROM Departmentinfo d WHERE d.name = :name")})
public class Departmentinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "departmentInfoId")
    private Integer departmentInfoId;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departmentInfoId")
    private List<Announcementinfo> announcementinfoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departmentInfoId")
    private List<Userinfo> userinfoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departmentInfoId")
    private List<Subjectinfo> subjectinfoList;

    public Departmentinfo() {
    }

    public Departmentinfo(Integer departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
    }

    public Departmentinfo(Integer departmentInfoId, String name) {
        this.departmentInfoId = departmentInfoId;
        this.name = name;
    }

    public Integer getDepartmentInfoId() {
        return departmentInfoId;
    }

    public void setDepartmentInfoId(Integer departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Announcementinfo> getAnnouncementinfoList() {
        return announcementinfoList;
    }

    public void setAnnouncementinfoList(List<Announcementinfo> announcementinfoList) {
        this.announcementinfoList = announcementinfoList;
    }

    @XmlTransient
    public List<Userinfo> getUserinfoList() {
        return userinfoList;
    }

    public void setUserinfoList(List<Userinfo> userinfoList) {
        this.userinfoList = userinfoList;
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
        hash += (departmentInfoId != null ? departmentInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departmentinfo)) {
            return false;
        }
        Departmentinfo other = (Departmentinfo) object;
        if ((this.departmentInfoId == null && other.departmentInfoId != null) || (this.departmentInfoId != null && !this.departmentInfoId.equals(other.departmentInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Departmentinfo[ departmentInfoId=" + departmentInfoId + " ]";
    }

}
