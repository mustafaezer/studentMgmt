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
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
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

    Transaction tx = null;

    @PostConstruct
    public void init() {
        grading = listAllGrades();
    }

    public List<Grading> listAllGrades() {
        grading = new ArrayList<>();

        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();

            Criteria cr = ses.createCriteria(Grading.class);
            cr.addOrder(Order.asc("grade"));

            grading = cr.list();

            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

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

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }
}
