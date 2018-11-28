/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Departmentinfo;
import com.pojos.Grading;
import com.pojos.Subjectinfo;
import com.pojos.Userinfo;
import com.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
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
public class NavbarBean {

    /**
     * Creates a new instance of NavbarBean
     */
    private String citizenshipNumber;
    private Departmentinfo departmentinfo;
    private String userType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private Date dateOfBirth;
    private String remark;
    private String isActive;
    private Set<Subjectinfo> subjectinfos = new HashSet<Subjectinfo>(0);
    private Set<Grading> gradings = new HashSet<Grading>(0);

    private Session ses;

    private List<Userinfo> loggedUserList;

    @PostConstruct
    public void init() {
        loggedUserList = loggedUser();
    }

    public List<Userinfo> loggedUser() {
        loggedUserList = new ArrayList<>();

        ses = HibernateUtil.getSessionFactory().openSession();
        ses.beginTransaction();

        String citizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

        Criteria criteria = ses.createCriteria(Userinfo.class);
        criteria.add(Restrictions.eq("citizenshipNumber", citizenshipNumber));
        loggedUserList = criteria.list();

        ses.getTransaction().commit();
        ses.close();

        return loggedUserList;
    }

    public NavbarBean() {
    }

    public List<Userinfo> getLoggedUserList() {
        return loggedUserList;
    }

    public void setLoggedUserList(List<Userinfo> loggedUserList) {
        this.loggedUserList = loggedUserList;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public Departmentinfo getDepartmentinfo() {
        return departmentinfo;
    }

    public void setDepartmentinfo(Departmentinfo departmentinfo) {
        this.departmentinfo = departmentinfo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Set<Subjectinfo> getSubjectinfos() {
        return subjectinfos;
    }

    public void setSubjectinfos(Set<Subjectinfo> subjectinfos) {
        this.subjectinfos = subjectinfos;
    }

    public Set<Grading> getGradings() {
        return gradings;
    }

    public void setGradings(Set<Grading> gradings) {
        this.gradings = gradings;
    }

}
