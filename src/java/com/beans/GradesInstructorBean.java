/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Grading;
import com.pojos.GradingId;
import com.pojos.Subjectinfo;
import com.pojos.Userinfo;
import com.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class GradesInstructorBean {

    /**
     * Creates a new instance of GradesInstructorBean
     */
    private Integer subjectInfoId;
    private String grade;
    private String gradeInline;

    private GradingId id;
    private Subjectinfo subjectinfo;
    private Userinfo userinfo;
    private Double midtermNote;
    private Double finaleNote;

    private List<Subjectinfo> coursesGiven;
    private List<String> gradesEnum;

    private Session ses;

    private Subjectinfo selectedSubject;
    private Grading selectedCourse;

    private List<Grading> gradingList;
    private List<Grading> temp;

    private String isRenderedP1 = "true";
    private String isCollapsedP1 = "false";
    private String isRenderedP2 = "false";
    private String isCollapsedP2 = "false";
    private String isRenderedP3 = "false";
    private String isCollapsedP3 = "false";

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();
        fetchCoursesGivenByInstructor();
        gradingList = new ArrayList<Grading>();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public GradesInstructorBean() {
    }

    public List<Subjectinfo> fetchCoursesGivenByInstructor() {
        coursesGiven = new ArrayList<Subjectinfo>();
        try {
            ses.beginTransaction();
            String instructorCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

            Criteria getCoursesGiven = ses.createCriteria(Subjectinfo.class);
            getCoursesGiven.add(Restrictions.eq("userinfo.citizenshipNumber", instructorCitizenshipNumber));
            coursesGiven = getCoursesGiven.list();

            ses.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coursesGiven;
    }

    public List<Grading> fillTableWithData() {
        gradingList = new ArrayList<Grading>();
        try {
            ses.beginTransaction();

            Criteria getGradingList = ses.createCriteria(Grading.class);
            getGradingList.add(Restrictions.eq("id.subjectInfoId", subjectInfoId));
            gradingList = getGradingList.list();

            ses.getTransaction().commit();
            isRenderedP2 = "true";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradingList;
    }

    public void fillStudentGradeInformation() {
        isCollapsedP1 = "true";
        isRenderedP1 = "true";
        isCollapsedP2 = "false";
        isRenderedP3 = "true";
        isCollapsedP3 = "false";
    }

    public void updateGrade() {
        isRenderedP3 = "false";
        isCollapsedP1 = "false";
        isCollapsedP2 = "false";
        try {
            ses.beginTransaction();
            
            GradingId idInstance = new GradingId();
            idInstance.setStudentCitizenshipNumber(selectedCourse.getId().getStudentCitizenshipNumber());
            idInstance.setSubjectInfoId(selectedCourse.getId().getSubjectInfoId());

            Grading gradingInstance = new Grading();
            
            Criteria getGradingToUpdate = ses.createCriteria(Grading.class);
            getGradingToUpdate.add(Restrictions.eq("id", idInstance));
            List<Grading> tempListToHoldGradingObject = getGradingToUpdate.list();
            gradingInstance = tempListToHoldGradingObject.get(0);
            
            gradingInstance.setMidtermNote(selectedCourse.getMidtermNote());
            gradingInstance.setFinaleNote(selectedCourse.getFinaleNote());
            gradingInstance.setGrade(calculateGrade(selectedCourse.getMidtermNote(),selectedCourse.getFinaleNote()));

            ses.saveOrUpdate(gradingInstance);
            ses.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void calculatedGrade() {
//        isCollapsedP2 = "true";
//        if (selectedCourse.getMidtermNote() != 0.0 && selectedCourse.getFinaleNote() != 0.0) {
//            gradeInline = calculateGrade(selectedCourse.getMidtermNote(), selectedCourse.getFinaleNote());
//            ses.beginTransaction();
//            try {
//                Criteria calculationUpdate = ses.createCriteria(Grading.class);
//                calculationUpdate.add(Restrictions.eq("id.subjectInfoId", selectedCourse.getId().getSubjectInfoId()));
//                calculationUpdate.add(Restrictions.eq("id.studentCitizenshipNumber", selectedCourse.getId().getStudentCitizenshipNumber()));
//                List<Grading> tempListToHoldGradingObject = calculationUpdate.list();
//                
//                Grading gradingToCalculate = new Grading();
//                gradingToCalculate = tempListToHoldGradingObject.get(0);
//                
//                gradingToCalculate.setGrade(gradeInline);
//                        
//                ses.saveOrUpdate(gradingToCalculate);
//                
//                ses.getTransaction().commit();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        isRenderedP3 = "false";
//        isCollapsedP1 = "false";
//        isCollapsedP2 = "false";
//    }

    public String calculateGrade(double midtermNote, double finaleNote) {
        String gradeTemp;
        if (midtermNote != 0.0 && finaleNote != 0.0) {
            double gradeDouble = (((midtermNote) * 0.4) + ((finaleNote) * 0.6));
            if (gradeDouble <= 20) {
                gradeTemp = "FF";
            } else if (gradeDouble > 20 && gradeDouble <= 30) {
                gradeTemp = "FD";
            } else if (gradeDouble > 30 && gradeDouble <= 40) {
                gradeTemp = "DD";
            } else if (gradeDouble > 40 && gradeDouble <= 50) {
                gradeTemp = "DC";
            } else if (gradeDouble > 50 && gradeDouble <= 60) {
                gradeTemp = "CC";
            } else if (gradeDouble > 60 && gradeDouble <= 70) {
                gradeTemp = "CB";
            } else if (gradeDouble > 70 && gradeDouble <= 80) {
                gradeTemp = "BB";
            } else if (gradeDouble > 80 && gradeDouble <= 90) {
                gradeTemp = "BA";
            } else {
                gradeTemp = "AA";
            }
        } else {
            gradeTemp = null;
        }
        return gradeTemp;
    }

    public void cancel() {
        isRenderedP3 = "false";
        isCollapsedP1 = "false";
        isCollapsedP2 = "false";
    }

    //getters & setters
    public Integer getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(Integer subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public List<Subjectinfo> getCoursesGiven() {
        return coursesGiven;
    }

    public void setCoursesGiven(List<Subjectinfo> courseGiven) {
        this.coursesGiven = courseGiven;
    }

    public Grading getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Grading selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<Grading> getGradingList() {
        return gradingList;
    }

    public void setGradingList(List<Grading> gradingList) {
        this.gradingList = gradingList;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIsRenderedP1() {
        return isRenderedP1;
    }

    public void setIsRenderedP1(String isRenderedP1) {
        this.isRenderedP1 = isRenderedP1;
    }

    public String getIsRenderedP2() {
        return isRenderedP2;
    }

    public void setIsRenderedP2(String isRenderedP2) {
        this.isRenderedP2 = isRenderedP2;
    }

    public String getIsRenderedP3() {
        return isRenderedP3;
    }

    public void setIsRenderedP3(String isRenderedP3) {
        this.isRenderedP3 = isRenderedP3;
    }

    public String getIsCollapsedP1() {
        return isCollapsedP1;
    }

    public void setIsCollapsedP1(String isCollapsedP1) {
        this.isCollapsedP1 = isCollapsedP1;
    }

    public String getIsCollapsedP2() {
        return isCollapsedP2;
    }

    public void setIsCollapsedP2(String isCollapsedP2) {
        this.isCollapsedP2 = isCollapsedP2;
    }

    public String getIsCollapsedP3() {
        return isCollapsedP3;
    }

    public void setIsCollapsedP3(String isCollapsedP3) {
        this.isCollapsedP3 = isCollapsedP3;
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

    public Subjectinfo getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subjectinfo selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

    public Subjectinfo getSub(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("no id provided");
        }
        for (Subjectinfo sub : coursesGiven) {
            if (id.equals(sub.getSubjectInfoId())) {
                return sub;
            }
        }
        return null;
    }

    public String getGradeInline() {
        return gradeInline;
    }

    public void setGradeInline(String gradeInline) {
        this.gradeInline = gradeInline;
    }

}
