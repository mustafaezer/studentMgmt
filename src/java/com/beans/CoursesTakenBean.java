/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Grading;
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
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class CoursesTakenBean {

    /**
     * Creates a new instance of CoursesTakenBean
     */
    private Session ses;

    private List<Grading> coursesList;

    Transaction tx = null;

    public CoursesTakenBean() {
    }

    @PostConstruct
    public void init() {
        coursesList = listCoursesTaken();
    }

    public List<Grading> listCoursesTaken() {
        coursesList = new ArrayList<>();
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            String studentCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");
            Criteria cr = ses.createCriteria(Grading.class);
            cr.add(Restrictions.eq("id.studentCitizenshipNumber", studentCitizenshipNumber));
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            coursesList = cr.list();
            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null || ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }
        return coursesList;
    }

    public List<Grading> getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(List<Grading> coursesList) {
        this.coursesList = coursesList;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

}
