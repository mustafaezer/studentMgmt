/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Grading;
import com.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class GradesBean {

    private Session ses;

    private List<Grading> grading;

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();
        grading = listAllGrades();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public List<Grading> listAllGrades() {
        grading = new ArrayList<>();

        try {
            ses.beginTransaction();

            Criteria cr = ses.createCriteria(Grading.class);
            cr.addOrder(Order.asc("grade"));

            grading = cr.list();

            ses.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return grading;
    }

    public List<Grading> getGrading() {
        return grading;
    }

    public void setGrading(List<Grading> grading) {
        this.grading = grading;
    }
}
