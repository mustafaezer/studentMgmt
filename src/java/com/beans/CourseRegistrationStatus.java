/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Runtimeproperties;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class CourseRegistrationStatus {

    /**
     * Creates a new instance of CourseRegistrationStatus
     */
    private Session ses;

    private String isDisabled;
    private String statusXHTML;
    private String nextState;

    private Integer propertyId;
    private String courseRegistrationStatus;

    List<Runtimeproperties> statusList;

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();

        fetchPresentStatus();
        isDisabled = enumToXHTML(courseRegistrationStatus);
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public String fetchPresentStatus() {
        statusList = new ArrayList<>();
        try {
            ses.beginTransaction();

            Criteria cr = ses.createCriteria(Runtimeproperties.class);
            cr.add(Restrictions.eq("propertyId", 1));
            statusList = cr.list();

            courseRegistrationStatus = statusList.get(0).getCourseRegistrationStatus();

            ses.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseRegistrationStatus;
    }

    public String enumToXHTML(String status) {
        if (status.equals("Enabled")) {
            statusXHTML = "false";
        } else if (status.equals("Disabled")) {
            statusXHTML = "true";
        }
        return statusXHTML;
    }

    public void enableCourseRegistration() {
        try {
            ses.beginTransaction();
            Criteria cr = ses.createCriteria(Runtimeproperties.class);
            cr.add(Restrictions.eq("propertyId", 1));
            Runtimeproperties presentState = (Runtimeproperties) cr.uniqueResult();
            
            nextState = "Enabled";
            presentState.setCourseRegistrationStatus(nextState);
            ses.update(presentState);
            ses.getTransaction().commit();
            
            isDisabled = "false";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Active", "Course registration & resigns are open."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableCourseRegistration() {
        try {
            ses.beginTransaction();
            Criteria cr = ses.createCriteria(Runtimeproperties.class);
            cr.add(Restrictions.eq("propertyId", 1));
            Runtimeproperties presentState = (Runtimeproperties) cr.uniqueResult();
            
            nextState = "Disabled";
            presentState.setCourseRegistrationStatus(nextState);
            ses.update(presentState);
            ses.getTransaction().commit();
            
            isDisabled = "true";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Inactive", "Course registration & resigns are closed."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CourseRegistrationStatus() {
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

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }
}
