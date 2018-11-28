/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

    /**
     *       Redirect to login page.      
     *
     * @return Login page name.
     */
    public String redirectToLogin() {
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     *       Go to login page.      
     *
     * @return Login page name.
     */
    public String toLogin() {
        return "/login.xhtml";
    }

    /**
     *       Redirect to welcome page.      
     *
     * @return Welcome page name.
     */
    public String redirectToWelcome() {
        return "/secured/dashboard.xhtml?faces-redirect=true";
    }

    /**
     *       Go to welcome page.      
     *
     * @return Welcome page name.
     */
    public String toWelcome() {
        return "/secured/dashboard.xhtml";
    }
}
