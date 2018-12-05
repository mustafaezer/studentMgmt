/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Turktrust Stajyer
 */
@Entity
@Table(name = "grading", catalog = "mgmt", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grading.findAll", query = "SELECT g FROM Grading g")
    , @NamedQuery(name = "Grading.findBySubjectInfoId", query = "SELECT g FROM Grading g WHERE g.gradingPK.subjectInfoId = :subjectInfoId")
    , @NamedQuery(name = "Grading.findByStudentCitizenshipNumber", query = "SELECT g FROM Grading g WHERE g.gradingPK.studentCitizenshipNumber = :studentCitizenshipNumber")
    , @NamedQuery(name = "Grading.findByMidtermNote", query = "SELECT g FROM Grading g WHERE g.midtermNote = :midtermNote")
    , @NamedQuery(name = "Grading.findByFinaleNote", query = "SELECT g FROM Grading g WHERE g.finaleNote = :finaleNote")
    , @NamedQuery(name = "Grading.findByGrade", query = "SELECT g FROM Grading g WHERE g.grade = :grade")})
public class Grading implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected GradingPK gradingPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Column(name = "midtermNote")
    private Double midtermNote;
    
    @Column(name = "finaleNote")
    private Double finaleNote;
    
    @Column(name = "grade")
    private String grade;
    
    @JoinColumn(name = "subjectInfoId", referencedColumnName = "subjectInfoId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Subjectinfo subjectinfo;
    
    @JoinColumn(name = "studentCitizenshipNumber", referencedColumnName = "citizenshipNumber", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Userinfo userinfo;

    public Grading() {
    }

    public Grading(GradingPK gradingPK) {
        this.gradingPK = gradingPK;
    }

    public Grading(int subjectInfoId, String studentCitizenshipNumber) {
        this.gradingPK = new GradingPK(subjectInfoId, studentCitizenshipNumber);
    }

    public GradingPK getGradingPK() {
        return gradingPK;
    }

    public void setGradingPK(GradingPK gradingPK) {
        this.gradingPK = gradingPK;
    }

    public Double getMidtermNote() {
        return midtermNote;
    }

    public void setMidtermNote(Double midtermNote) {
        this.midtermNote = midtermNote;
    }

    public Double getFinaleNote() {
        return finaleNote;
    }

    public void setFinaleNote(Double finaleNote) {
        this.finaleNote = finaleNote;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Subjectinfo getSubjectinfo() {
        return subjectinfo;
    }

    public void setSubjectinfo(Subjectinfo subjectinfo) {
        this.subjectinfo = subjectinfo;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gradingPK != null ? gradingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grading)) {
            return false;
        }
        Grading other = (Grading) object;
        if ((this.gradingPK == null && other.gradingPK != null) || (this.gradingPK != null && !this.gradingPK.equals(other.gradingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Grading[ gradingPK=" + gradingPK + " ]";
    }
    
}
