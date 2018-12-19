/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Departmentinfo;
import com.entity.Grading;
import com.entity.GradingPK;
import com.entity.Subjectinfo;
import com.entity.Userinfo;
import com.util.HibernateUtil;
import java.io.Serializable;
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
import org.primefaces.model.DualListModel;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class CourseRegistrationBean implements Serializable {

    private Session ses;

    private Integer subjectInfoId;
    private Departmentinfo departmentinfo;
    private String instructorCitizenshipNumber;
    private String name;
    private int quota;
    private String year;
    private String season;
    private String isActive;

    private String collapseStateDataTable;
    private String collapseStatePickList;

    private Subjectinfo selectedSubject;

    List<Subjectinfo> temporaryL;
    List<Subjectinfo> subjectListDualSource;
    List<Subjectinfo> subjectListDualTarget;
    List<Subjectinfo> registeredSubjects;
    List tempIDs;
    List<Subjectinfo> source;
    List<Subjectinfo> target;
    List<Grading> notInList;
    List<Integer> notInListIDs;
    List<Integer> registeredSubjectIDs;

    List<Userinfo> user;

    private String isRenderedP1;
    private String isRenderedP2;
    private String isRenderedP3;

    private String isCollapsedP1;
    private String isCollapsedP2;
    private String isCollapsedP3;

    private DualListModel<Subjectinfo> subjectListDual;

    Transaction tx = null;

    @PostConstruct
    public void init() {
        List<Subjectinfo> source = fetchSubjectListDualSource();
        List<Subjectinfo> target = new ArrayList<>();

        subjectListDual = new DualListModel<>(source, target);

        listRegisteredSubjects();

        isRenderedP1 = "true";
        isRenderedP2 = "true";
        isRenderedP3 = "true";
        isCollapsedP1 = "true";
        isCollapsedP2 = "false";
        isCollapsedP3 = "false";
    }

    public List<Subjectinfo> fetchSubjectListDualSource() {
        subjectListDualSource = new ArrayList<>();

        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            String studentCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

            // Get department information ID of student who is in active session.
            Criteria getDepartment = ses.createCriteria(Userinfo.class);
            getDepartment.add(Restrictions.eq("citizenshipNumber", studentCitizenshipNumber));
            List<Userinfo> tempListToHoldUserObject = getDepartment.list();
            int departmentInfoId = tempListToHoldUserObject.get(0).getDepartmentInfoId().getDepartmentInfoId();

            // Courses taken by student who is in active session.
            Criteria subQueryCriteriaForNotIn = ses.createCriteria(Grading.class);
            subQueryCriteriaForNotIn.add(Restrictions.eq("id.studentCitizenshipNumber", studentCitizenshipNumber));
            subQueryCriteriaForNotIn.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            notInList = subQueryCriteriaForNotIn.list();

            // IDs of taken courses by student who is in active session.
            notInListIDs = new ArrayList<Integer>();
            for (int i = 0; i < notInList.size(); i++) {
                Integer ID = notInList.get(i).getGradingPK().getSubjectInfoId();
                notInListIDs.add(ID);
            }

            // Execution of creating DualListModel's source list.
            Criteria mainCriteriaForDualSource = ses.createCriteria(Subjectinfo.class);
            mainCriteriaForDualSource.add(
                    Restrictions.not(
                            Restrictions.in("subjectInfoId", notInListIDs)
                    )
            );
            mainCriteriaForDualSource.add(Restrictions.eq("departmentInfoId.departmentInfoId", departmentInfoId));

            subjectListDualSource = mainCriteriaForDualSource.list();
            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }
        return subjectListDualSource;
    }

    public List<Subjectinfo> listRegisteredSubjects() {
        registeredSubjects = new ArrayList<>();

        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            String studentCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

            // Courses taken by student who is in active session.
            Criteria subQueryCriteriaForNotIn = ses.createCriteria(Grading.class);
            subQueryCriteriaForNotIn.add(Restrictions.eq("gradingPK.studentCitizenshipNumber", studentCitizenshipNumber));
            subQueryCriteriaForNotIn.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            registeredSubjects = subQueryCriteriaForNotIn.list();

            // IDs of taken courses by student who is in active session.
            registeredSubjectIDs = new ArrayList<Integer>();
            for (int i = 0; i < notInList.size(); i++) {
                Integer ID = notInList.get(i).getGradingPK().getSubjectInfoId();
                registeredSubjectIDs.add(ID);
            }

            // Execution of creating registered courses list.
            Criteria mainCriteriaForRegisteredSubjects = ses.createCriteria(Subjectinfo.class);
            mainCriteriaForRegisteredSubjects.add(
                    Restrictions.in("subjectInfoId", registeredSubjectIDs)
            );

            registeredSubjects = mainCriteriaForRegisteredSubjects.list();
            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }
        return registeredSubjects;
    }

    public void register() {

        List<Subjectinfo> target = subjectListDual.getTarget();

        if (!target.isEmpty()) {
            try {

                String studentCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

                for (int i = 0; i < target.size(); i++) {
                    ses = HibernateUtil.getSessionFactory().openSession();
                    tx = ses.beginTransaction();

                    Integer ID = target.get(i).getSubjectInfoId();

                    Grading registerToCourse = new Grading();
                    GradingPK registerToCourseId = new GradingPK();
                    registerToCourseId.setSubjectInfoId(ID);
                    registerToCourseId.setStudentCitizenshipNumber(studentCitizenshipNumber);
                    registerToCourse.setGradingPK(registerToCourseId);
                    ses.save(registerToCourse);

                    Criteria getQuotaAsList = ses.createCriteria(Subjectinfo.class);
                    getQuotaAsList.add(Restrictions.eq("subjectInfoId", ID));
                    List<Subjectinfo> tempListForHoldTheSubjectObject = getQuotaAsList.list();
                    int quota = tempListForHoldTheSubjectObject.get(0).getQuota();

                    quota = quota - 1;

                    Subjectinfo subjectToUpdate = new Subjectinfo();
                    Criteria getSubjectToUpdate = ses.createCriteria(Subjectinfo.class);
                    getSubjectToUpdate.add(Restrictions.eq("subjectInfoId", ID));
                    List<Subjectinfo> tempListForHoldToSpecificSubjectObject = getSubjectToUpdate.list();
                    subjectToUpdate = tempListForHoldToSpecificSubjectObject.get(0);
                    subjectToUpdate.setQuota(quota);
                    ses.update(subjectToUpdate);
                    tx.commit();
                    ses.close();
                }

                pageReset();

                isRenderedP1 = "true";
                isRenderedP2 = "true";
                isRenderedP3 = "true";
                isCollapsedP1 = "true";
                isCollapsedP2 = "false";
                isCollapsedP3 = "false";

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful", "You have successfully registered to courses in target list."));
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }

                if (ses != null && ses.isOpen()) {
                    ses.close();
                    ses = null;
                }

                e.printStackTrace();
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error", "Please move at least one subject to target list first to proceed this event."));
        }
    }

    public void resign() {
        if (selectedSubject != null) {
            try {
                ses = HibernateUtil.getSessionFactory().openSession();

                tx = ses.beginTransaction();
                String studentCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

                GradingPK gradingIdToDelete = new GradingPK();
                gradingIdToDelete.setSubjectInfoId(selectedSubject.getSubjectInfoId());
                gradingIdToDelete.setStudentCitizenshipNumber(studentCitizenshipNumber);

                Criteria getGradingToDelete = ses.createCriteria(Grading.class);
                getGradingToDelete.add(Restrictions.eq("id", gradingIdToDelete));
                List<Grading> tempListToHoldGrading = getGradingToDelete.list();
                Grading gradingToDelete = new Grading();
                gradingToDelete = tempListToHoldGrading.get(0);
                ses.delete(gradingToDelete);

                Criteria getQuotaAsList = ses.createCriteria(Subjectinfo.class);
                getQuotaAsList.add(Restrictions.eq("subjectInfoId", selectedSubject.getSubjectInfoId()));
                List<Subjectinfo> tempListForHoldTheSubjectObject = getQuotaAsList.list();
                int quota = tempListForHoldTheSubjectObject.get(0).getQuota();

                quota = quota + 1;

                Subjectinfo subjectToUpdate = new Subjectinfo();
                Criteria getSubjectToUpdate = ses.createCriteria(Subjectinfo.class);
                getSubjectToUpdate.add(Restrictions.eq("subjectInfoId", selectedSubject.getSubjectInfoId()));
                List<Subjectinfo> tempListForHoldToSpecificSubjectObject = getSubjectToUpdate.list();
                subjectToUpdate = tempListForHoldToSpecificSubjectObject.get(0);
                subjectToUpdate.setQuota(quota);
                ses.update(subjectToUpdate);
                tx.commit();
                ses.close();

                pageReset();

                isRenderedP1 = "true";
                isRenderedP2 = "true";
                isRenderedP3 = "true";
                isCollapsedP1 = "true";
                isCollapsedP2 = "false";
                isCollapsedP3 = "false";

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resignation Successful", "You have successfully resigned from " + selectedSubject.getName()));

            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }

                if (ses != null && ses.isOpen()) {
                    ses.close();
                    ses = null;
                }

                e.printStackTrace();
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Resignation Error", "Please select a course first to proceed this event."));
        }
    }

    public void pageReset() {
        List<Subjectinfo> source = fetchSubjectListDualSource();
        List<Subjectinfo> target = new ArrayList<>();
        subjectListDual = new DualListModel<>(source, target);

        registeredSubjects = listRegisteredSubjects();
    }

    public String getCollapseStateDataTable() {
        return collapseStateDataTable;
    }

    public void setCollapseStateDataTable(String collapseStateDataTable) {
        this.collapseStateDataTable = collapseStateDataTable;
    }

    public String getCollapseStatePickList() {
        return collapseStatePickList;
    }

    public void setCollapseStatePickList(String collapseStatePickList) {
        this.collapseStatePickList = collapseStatePickList;
    }

    public List<Subjectinfo> getRegisteredSubjects() {
        return registeredSubjects;
    }

    public void setRegisteredSubjects(List<Subjectinfo> registeredSubjects) {
        this.registeredSubjects = registeredSubjects;
    }

    public CourseRegistrationBean() {
    }

    public Subjectinfo getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subjectinfo selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

    public DualListModel<Subjectinfo> getSubjectListDual() {
        return subjectListDual;
    }

    public void setSubjectListDual(DualListModel<Subjectinfo> subjectListDual) {
        this.subjectListDual = subjectListDual;
    }

    public Integer getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(Integer subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public Departmentinfo getDepartmentinfo() {
        return departmentinfo;
    }

    public void setDepartmentinfo(Departmentinfo departmentinfo) {
        this.departmentinfo = departmentinfo;
    }

    public String getInstructorCitizenshipNumber() {
        return instructorCitizenshipNumber;
    }

    public void setInstructorCitizenshipNumber(String instructorCitizenshipNumber) {
        this.instructorCitizenshipNumber = instructorCitizenshipNumber;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public List<Subjectinfo> getSource() {
        return source;
    }

    public void setSource(List<Subjectinfo> source) {
        this.source = source;
    }

    public List<Subjectinfo> getTarget() {
        return target;
    }

    public void setTarget(List<Subjectinfo> target) {
        this.target = target;
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

    public List<Grading> getNotInList() {
        return notInList;
    }

    public void setNotInList(List<Grading> notInList) {
        this.notInList = notInList;
    }

}
