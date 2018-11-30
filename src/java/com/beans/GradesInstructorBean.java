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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

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
    private String isRenderedP4 = "false";
    private String isCollapsedP4 = "true";
    private String isRenderedMidtermButton = "false";
    private String isRenderedFinaleButton = "false";

    Transaction tx = null;

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
        if (subjectInfoId != null) {
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
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selection Warning", "Please select a course first to proceed this event."));
            return null;
        }
    }

    public void midtermPanelInitializer() {
        if (selectedCourse != null) {
            isCollapsedP1 = "true";
            isRenderedP1 = "true";
            isCollapsedP2 = "false";
            isRenderedP3 = "true";
            isCollapsedP3 = "false";
            isRenderedP4 = "false";
            isCollapsedP4 = "true";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selection Warning", "Please select a student first to proceed this event."));
        }
    }

    public void finalePanelInitializer() {
        if (selectedCourse.getMidtermNote() != null) {
            isCollapsedP1 = "true";
            isRenderedP1 = "true";
            isCollapsedP2 = "false";
            isRenderedP3 = "false";
            isCollapsedP3 = "true";
            isRenderedP4 = "true";
            isCollapsedP4 = "false";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Edit Finale Note Error", "Please first insert midterm note of student to proceed this event."));
        }
    }

    public void insertMidterm() {
        if (selectedCourse.getMidtermNote() != null) {
            try {
                tx = ses.beginTransaction();

                selectedCourse.setMidtermNote(selectedCourse.getMidtermNote());
                ses.update(selectedCourse);

                tx.commit();

                if (!selectedCourse.getMidtermNote().equals(null) && !selectedCourse.getFinaleNote().equals(null)) {
                    calculateGrade(selectedCourse.getMidtermNote(), selectedCourse.getFinaleNote());
                }

                gradingList = fillTableWithData();

                isRenderedP3 = "false";
                isRenderedP4 = "false";
                isCollapsedP1 = "false";
                isCollapsedP2 = "false";

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Midterm has been successfully updated."));
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Edit Midterm Error", "Please type a valid value for midterm note. Notes must be in range of 0-100."));
        }
    }

    public void insertFinale() {
        if (selectedCourse.getMidtermNote() != null) {
            try {
                tx = ses.beginTransaction();

                selectedCourse.setFinaleNote(selectedCourse.getFinaleNote());
                ses.update(selectedCourse);

                tx.commit();

                calculateGrade(selectedCourse.getMidtermNote(), selectedCourse.getFinaleNote());

                gradingList = fillTableWithData();

                isRenderedP3 = "false";
                isRenderedP4 = "false";
                isCollapsedP1 = "false";
                isCollapsedP2 = "false";

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Finale has been successfully updated."));

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            }
        }

    }

    public void calculateGrade(double midtermNote, double finaleNote) {
        try {
            tx = ses.beginTransaction();

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

            selectedCourse.setGrade(gradeTemp);
            ses.update(selectedCourse);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

    }

    public void cancel() {
        isRenderedP3 = "false";
        isRenderedP4 = "false";
        isCollapsedP1 = "false";
        isCollapsedP2 = "false";
    }

    public void onRowSelect(SelectEvent event) {
        isRenderedFinaleButton = "true";
        isRenderedMidtermButton = "true";
    }

    public void onRowUnselect(UnselectEvent event) {
        isRenderedMidtermButton = "false";
        isRenderedFinaleButton = "false";
        isCollapsedP1 = "false";
        isRenderedP3 = "false";
        isRenderedP4 = "false";
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

    public String getIsRenderedP4() {
        return isRenderedP4;
    }

    public void setIsRenderedP4(String isRenderedP4) {
        this.isRenderedP4 = isRenderedP4;
    }

    public String getIsCollapsedP4() {
        return isCollapsedP4;
    }

    public void setIsCollapsedP4(String isCollapsedP4) {
        this.isCollapsedP4 = isCollapsedP4;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    public String getIsRenderedMidtermButton() {
        return isRenderedMidtermButton;
    }

    public void setIsRenderedMidtermButton(String isRenderedMidtermButton) {
        this.isRenderedMidtermButton = isRenderedMidtermButton;
    }

    public String getIsRenderedFinaleButton() {
        return isRenderedFinaleButton;
    }

    public void setIsRenderedFinaleButton(String isRenderedFinaleButton) {
        this.isRenderedFinaleButton = isRenderedFinaleButton;
    }

}
