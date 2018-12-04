/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Departmentinfo;
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
import org.hibernate.criterion.Order;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class SubjectBean implements Serializable {

    private Session ses;
    private List<Subjectinfo> subjectList;

    private Integer subjectInfoId;
    private Departmentinfo departmentinfo;
    private Userinfo userinfo;
    private String name;
    private int quota;
    private String year;
    private String season;
    private String isActive;

    private Subjectinfo selectedSubject;

    private String isCollapsedP1;
    private String isCollapsedP2;
    private String isCollapsedP3;

    private String isRenderedP1;
    private String isRenderedP2;
    private String isRenderedP3;
    private String isRenderedEditUserButton = "false";

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();

        isCollapsedP1 = "false";
        isCollapsedP2 = "true";
        isCollapsedP3 = "true";

        isRenderedP1 = "true";
        isRenderedP2 = "false";
        isRenderedP3 = "false";

        listAllSubjects();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public List<Subjectinfo> listAllSubjects() {
        subjectList = new ArrayList<>();
        try {
            ses.beginTransaction();
            Criteria cr = ses.createCriteria(Subjectinfo.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.addOrder(Order.asc("isActive"));
            cr.addOrder(Order.asc("departmentInfoId.departmentInfoId"));
            cr.addOrder(Order.asc("year"));
            cr.addOrder(Order.asc("season"));
            cr.addOrder(Order.asc("instructorCitizenshipNumber.citizenshipNumber"));
            cr.addOrder(Order.asc("name"));
            subjectList = cr.list();
            ses.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectList;
    }

    public void createNewSubjectPanelInitializer() {
        isRenderedP2 = "true";
        isRenderedP1 = "true";
        isRenderedP3 = "false";

        isCollapsedP2 = "false";
        isCollapsedP1 = "true";
        isCollapsedP3 = "true";
    }

    public void createNewSubject() {
        try {
            ses.beginTransaction();
            Subjectinfo newSubject = new Subjectinfo();
            newSubject.setSubjectInfoId(null);
            newSubject.setDepartmentInfoId(departmentinfo);
            newSubject.setInstructorCitizenshipNumber(userinfo);
            newSubject.setName(name);
            newSubject.setQuota(quota);
            newSubject.setYear(year);
            newSubject.setSeason(season);
            newSubject.setIsActive(isActive);
            ses.save(newSubject);
            ses.getTransaction().commit();

            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "false";

            isCollapsedP1 = "false";
            isCollapsedP2 = "true";
            isCollapsedP3 = "true";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Subject Creation Successful", "New subject has been successfully created with the informations given."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editSubjectPanelInitializer() {
        if (selectedSubject != null) {
            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "true";

            isCollapsedP1 = "true";
            isCollapsedP2 = "true";
            isCollapsedP3 = "false";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Edit Subject Information", "Please select a subject first to proceed this event."));
        }
    }

    public void updateSubject() {
        if (selectedSubject != null) {
            if (selectedSubject.getIsActive() != "" && selectedSubject.getDepartmentInfoId()!= null && selectedSubject.getYear() != "" && selectedSubject.getSeason() != "" && selectedSubject.getInstructorCitizenshipNumber()!= null && selectedSubject.getName() != "" && selectedSubject.getQuota() > 0) {
                try {
                    ses.beginTransaction();

                    selectedSubject.setIsActive(selectedSubject.getIsActive());
                    selectedSubject.setDepartmentInfoId(selectedSubject.getDepartmentInfoId());
                    selectedSubject.setYear(selectedSubject.getYear());
                    selectedSubject.setSeason(selectedSubject.getSeason());
                    selectedSubject.setInstructorCitizenshipNumber(selectedSubject.getInstructorCitizenshipNumber());
                    selectedSubject.setName(selectedSubject.getName());
                    selectedSubject.setQuota(selectedSubject.getQuota());
                    ses.update(selectedSubject);
                    ses.getTransaction().commit();

                    isRenderedP1 = "true";
                    isRenderedP2 = "false";
                    isRenderedP3 = "false";

                    isCollapsedP1 = "false";
                    isCollapsedP2 = "true";
                    isCollapsedP3 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful", "You have successfully updated informations of subject selected."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Edit Subject Error", "Please fill the all required fields for subject to proceed this event."));
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Edit Subject Information", "Please select a subject first to proceed this event."));
        }
    }

    public void cancel() {
        isRenderedP1 = "true";
        isRenderedP2 = "false";
        isRenderedP3 = "false";

        isCollapsedP1 = "false";
        isCollapsedP2 = "true";
        isCollapsedP3 = "true";
    }

    public void onRowSelect(SelectEvent event) {
        isRenderedEditUserButton = "true";
    }

    public void onRowUnselect(UnselectEvent event) {
        isRenderedEditUserButton = "false";
    }

    public SubjectBean() {
    }

    public Userinfo getUserinfo() {
        if (userinfo == null) {
            userinfo = new Userinfo();
            return userinfo;
        } else {
            return userinfo;
        }
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public Integer getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(Integer subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public Departmentinfo getDepartmentinfo() {
        if (departmentinfo == null) {
            departmentinfo = new Departmentinfo();
            return departmentinfo;
        } else {
            return departmentinfo;
        }
    }

    public void setDepartmentinfo(Departmentinfo departmentinfo) {
        this.departmentinfo = departmentinfo;
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

    public List<Subjectinfo> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subjectinfo> subjectList) {
        this.subjectList = subjectList;
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

    public Subjectinfo getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(Subjectinfo selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

    public String getIsRenderedEditUserButton() {
        return isRenderedEditUserButton;
    }

    public void setIsRenderedEditUserButton(String isRenderedEditUserButton) {
        this.isRenderedEditUserButton = isRenderedEditUserButton;
    }

}
