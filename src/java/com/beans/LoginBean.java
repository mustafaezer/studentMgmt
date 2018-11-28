/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Userinfo;
import java.io.Serializable;
import com.util.HibernateUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private Session ses;

    private String citizenshipNumber;
    private String password;
    public boolean isLoggedIn;

    private List<Userinfo> list;

    private String isRenderedIndeterminate;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();
        isLoggedIn = false;
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public LoginBean() {
    }

    /**
     *       Login operation.      
     *
     * @return
     */
    public String checkUser() {

        try {
            ses.beginTransaction();

            Criteria cr = ses.createCriteria(Userinfo.class);
            cr.add(Restrictions.eq("citizenshipNumber", citizenshipNumber));
            cr.add(Restrictions.eq("password", UserBean.getSHA256(password)));
            List<Userinfo> list = cr.list();

            ses.getTransaction().commit();

            if (list.size() == 1) {
                isLoggedIn = true;

                FacesMessage msg = new FacesMessage("Welcome", "You've successfully accessed to the dashboard of Student Management System.");
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                FacesContext.getCurrentInstance().addMessage(null, msg);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("citizenshipNumber", citizenshipNumber);

                return navigationBean.redirectToWelcome();
            } else {
                FacesMessage msg = new FacesMessage("Login Error", "Check your citizenship number or password again.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);

                return navigationBean.toLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return navigationBean.toLogin();
        }
    }

    /**
     *       Logout operation.      
     *
     * @return
     */
    public String userLogout() {
        isLoggedIn = false;

        FacesMessage msg = new FacesMessage("Logged out", "You've successfully signed out from Student Management System.");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return "/login.xhtml";
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public String returnCitizenshipNumber() {
        return citizenshipNumber;
    }

    public String getIsRenderedIndeterminate() {
        return isRenderedIndeterminate;
    }

    public void setIsRenderedIndeterminate(String isRenderedIndeterminate) {
        this.isRenderedIndeterminate = isRenderedIndeterminate;
    }
}
