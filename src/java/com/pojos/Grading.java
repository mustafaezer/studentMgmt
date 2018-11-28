package com.pojos;
// Generated Nov 14, 2018 4:08:11 PM by Hibernate Tools 4.3.1



/**
 * Grading generated by hbm2java
 */
public class Grading  implements java.io.Serializable {


     private GradingId id;
     private Subjectinfo subjectinfo;
     private Userinfo userinfo;
     private Double midtermNote;
     private Double finaleNote;
     private String grade;

    public Grading() {
    }

	
    public Grading(GradingId id, Subjectinfo subjectinfo, Userinfo userinfo) {
        this.id = id;
        this.subjectinfo = subjectinfo;
        this.userinfo = userinfo;
    }
    public Grading(GradingId id, Subjectinfo subjectinfo, Userinfo userinfo, Double midtermNote, Double finaleNote, String grade) {
       this.id = id;
       this.subjectinfo = subjectinfo;
       this.userinfo = userinfo;
       this.midtermNote = midtermNote;
       this.finaleNote = finaleNote;
       this.grade = grade;
    }
   
    public GradingId getId() {
        return this.id;
    }
    
    public void setId(GradingId id) {
        this.id = id;
    }
    public Subjectinfo getSubjectinfo() {
        return this.subjectinfo;
    }
    
    public void setSubjectinfo(Subjectinfo subjectinfo) {
        this.subjectinfo = subjectinfo;
    }
    public Userinfo getUserinfo() {
        return this.userinfo;
    }
    
    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }
    public Double getMidtermNote() {
        return this.midtermNote;
    }
    
    public void setMidtermNote(Double midtermNote) {
        this.midtermNote = midtermNote;
    }
    public Double getFinaleNote() {
        return this.finaleNote;
    }
    
    public void setFinaleNote(Double finaleNote) {
        this.finaleNote = finaleNote;
    }
    public String getGrade() {
        return this.grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }




}


