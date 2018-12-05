/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Turktrust Stajyer
 */
@Embeddable
public class GradingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "subjectInfoId")
    private int subjectInfoId;

    @Basic(optional = false)
    @Column(name = "studentCitizenshipNumber")
    private String studentCitizenshipNumber;

    public GradingPK() {
    }

    public GradingPK(int subjectInfoId, String studentCitizenshipNumber) {
        this.subjectInfoId = subjectInfoId;
        this.studentCitizenshipNumber = studentCitizenshipNumber;
    }

    public int getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(int subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public String getStudentCitizenshipNumber() {
        return studentCitizenshipNumber;
    }

    public void setStudentCitizenshipNumber(String studentCitizenshipNumber) {
        this.studentCitizenshipNumber = studentCitizenshipNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) subjectInfoId;
        hash += (studentCitizenshipNumber != null ? studentCitizenshipNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GradingPK)) {
            return false;
        }
        GradingPK other = (GradingPK) object;
        if (this.subjectInfoId != other.subjectInfoId) {
            return false;
        }
        if ((this.studentCitizenshipNumber == null && other.studentCitizenshipNumber != null) || (this.studentCitizenshipNumber != null && !this.studentCitizenshipNumber.equals(other.studentCitizenshipNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GradingPK[ subjectInfoId=" + subjectInfoId + ", studentCitizenshipNumber=" + studentCitizenshipNumber + " ]";
    }

}
