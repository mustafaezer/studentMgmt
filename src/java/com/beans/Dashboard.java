/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Departmentinfo;
import com.pojos.Subjectinfo;
import com.pojos.Userinfo;
import com.util.HibernateUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class Dashboard implements Serializable {

    /**
     * Creates a new instance of Dashboard
     */
    private Session ses;

    private int departmentCount;
    private int instructorCount;
    private int studentCount;
    private int subjectCount;

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();
        populateStatistics();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public Dashboard() {
    }

    public void populateStatistics() {
        try {
            departmentCount = calculateDepartmentCount();
            instructorCount = calculateInstructorCount();
            studentCount = calculateStudentCount();
            subjectCount = calculateSubjectCount();

        } catch (Exception e) {

            e.printStackTrace();

            departmentCount = 0;
            instructorCount = 0;
            studentCount = 0;
            subjectCount = 0;
        }
    }

    public int calculateDepartmentCount() {
        ses.beginTransaction();

        Criteria getDepartmentCountCriteria = ses.createCriteria(Departmentinfo.class);
        getDepartmentCountCriteria.setProjection(Projections.rowCount());
        departmentCount = Integer.parseInt(getDepartmentCountCriteria.uniqueResult().toString());

        ses.getTransaction().commit();

        return departmentCount;
    }

    public int calculateInstructorCount() {
        ses.beginTransaction();

        Criteria getInstructorCountCriteria = ses.createCriteria(Userinfo.class);
        getInstructorCountCriteria.add(Restrictions.eq("userType", "Instructor"));
        getInstructorCountCriteria.setProjection(Projections.rowCount());
        instructorCount = Integer.parseInt(getInstructorCountCriteria.uniqueResult().toString());

        ses.getTransaction().commit();

        return instructorCount;
    }

    public int calculateStudentCount() {
        ses.beginTransaction();

        Criteria getStudentCountCriteria = ses.createCriteria(Userinfo.class);
        getStudentCountCriteria.add(Restrictions.eq("userType", "Student"));
        getStudentCountCriteria.setProjection(Projections.rowCount());
        studentCount = Integer.parseInt(getStudentCountCriteria.uniqueResult().toString());

        ses.getTransaction().commit();

        return studentCount;
    }

    public int calculateSubjectCount() {
        ses.beginTransaction();

        Criteria getSubjectCountCriteria = ses.createCriteria(Subjectinfo.class);
        getSubjectCountCriteria.setProjection(Projections.rowCount());
        subjectCount = Integer.parseInt(getSubjectCountCriteria.uniqueResult().toString());

        ses.getTransaction().commit();

        return subjectCount;
    }

    public int getDepartmentCount() {
        return departmentCount;
    }

    public void setDepartmentCount(int departmentCount) {
        this.departmentCount = departmentCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getInstructorCount() {
        return instructorCount;
    }

    public void setInstructorCount(int instructorCount) {
        this.instructorCount = instructorCount;
    }

    public int getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(int subjectCount) {
        this.subjectCount = subjectCount;
    }

}
