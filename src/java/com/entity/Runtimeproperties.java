/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Turktrust Stajyer
 */
@Entity
@Table(name = "runtimeproperties", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Runtimeproperties.findAll", query = "SELECT r FROM Runtimeproperties r")
    , @NamedQuery(name = "Runtimeproperties.findByPropertyId", query = "SELECT r FROM Runtimeproperties r WHERE r.propertyId = :propertyId")
    , @NamedQuery(name = "Runtimeproperties.findByCourseRegistrationStatus", query = "SELECT r FROM Runtimeproperties r WHERE r.courseRegistrationStatus = :courseRegistrationStatus")})
public class Runtimeproperties implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "propertyId")
    
    private Integer propertyId;
    @Column(name = "courseRegistrationStatus")
    private String courseRegistrationStatus;

    public Runtimeproperties() {
    }

    public Runtimeproperties(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public String getCourseRegistrationStatus() {
        return courseRegistrationStatus;
    }

    public void setCourseRegistrationStatus(String courseRegistrationStatus) {
        this.courseRegistrationStatus = courseRegistrationStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (propertyId != null ? propertyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Runtimeproperties)) {
            return false;
        }
        Runtimeproperties other = (Runtimeproperties) object;
        if ((this.propertyId == null && other.propertyId != null) || (this.propertyId != null && !this.propertyId.equals(other.propertyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Runtimeproperties[ propertyId=" + propertyId + " ]";
    }
    
}
