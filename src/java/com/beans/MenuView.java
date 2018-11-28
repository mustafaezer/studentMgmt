/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@SessionScoped
public class MenuView {

    /**
     * Creates a new instance of MenuView
     */
    private MenuModel model;

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();

        DefaultSubMenu management = new DefaultSubMenu("Management");
        
        DefaultMenuItem dashboard = new DefaultMenuItem("Dashboard");
        
        management.addElement(dashboard);
    }

    public MenuView() {
    }

    public MenuModel getModel() {
        return model;
    }
    
}
