/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Announcementinfo;
import com.entity.Departmentinfo;
import com.entity.Userinfo;
import com.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class AnnouncementBean {

    /**
     * Creates a new instance of AnnouncementBean
     */
    @PostConstruct
    public void init() {
        departmentList = listAllDepartments();
        announcementList = listAllAnnouncements();

        isRenderedP0 = "true";
        isCollapsedP0 = "true";

        isRenderedP1 = "true";
        isCollapsedP1 = "false";

        isRenderedP2 = "false";
        isCollapsedP2 = "true";
    }

    private Session ses;

    private String isRenderedP0;
    private String isRenderedP1;
    private String isRenderedP2;

    private String isCollapsedP0;
    private String isCollapsedP1;
    private String isCollapsedP2;

    private List<Departmentinfo> departmentList;
    private List<Announcementinfo> announcementList;

    private Announcementinfo selectedAnnouncement;

    private Integer announcementId;
    private Departmentinfo departmentinfo;
    private Userinfo userinfo;
    private String type;
    private String importance;
    private String header;
    private String context;
    private Date startDate;
    private Date endDate;
    private String isActive;

    Transaction tx = null;

    public AnnouncementBean() {

    }

    public void createAnnouncement() {
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();

            if (departmentinfo != null && type != null && importance != null && header != "" && context != "" && startDate != null && endDate != null) {
                Announcementinfo newAnnouncement = new Announcementinfo();

                newAnnouncement.setDepartmentInfoId(departmentinfo);

                String citizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");
                Criteria getUserinfoCr = ses.createCriteria(Userinfo.class);
                getUserinfoCr.add(Restrictions.eq("citizenshipNumber", citizenshipNumber));
                List<Userinfo> temp = getUserinfoCr.list();
                Userinfo userinfoTemp = temp.get(0);
                newAnnouncement.setAuthorCitizenshipNumber(userinfoTemp);

                newAnnouncement.setType(type);
                newAnnouncement.setImportance(importance);
                newAnnouncement.setHeader(header);
                newAnnouncement.setContext(context);
                newAnnouncement.setStartDate(startDate);
                newAnnouncement.setEndDate(endDate);
                newAnnouncement.setIsActive(isActive);

                ses.save(newAnnouncement);
                tx.commit();
                ses.close();

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement Created", "Announcement has been successfully created: " + newAnnouncement.getHeader()));

                isCollapsedP0 = "true";
                isCollapsedP1 = "false";
                isCollapsedP2 = "true";

                isRenderedP0 = "true";
                isRenderedP1 = "true";
                isRenderedP2 = "false";

                announcementList = listAllAnnouncements();

            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement Creation Error", "Please fill all the required fields for an announcement."));
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }

    }

    public List<Announcementinfo> listAllAnnouncements() {
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();

            announcementList = new ArrayList<>();
            Criteria listAnnouncements = ses.createCriteria(Announcementinfo.class);
            announcementList = listAnnouncements.list();

            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }
        return announcementList;
    }

    public void cancel() {
        isCollapsedP0 = "true";
        isCollapsedP1 = "false";
        isCollapsedP2 = "true";

        isRenderedP0 = "true";
        isRenderedP1 = "true";
        isRenderedP2 = "false";
    }

    public List<Departmentinfo> listAllDepartments() {
        departmentList = new ArrayList<>();

        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            Criteria cr = ses.createCriteria(Departmentinfo.class);
            cr.addOrder(Order.asc("name"));
            departmentList = cr.list();
            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }

        return departmentList;
    }

    public void onRowSelect(SelectEvent event) {
        isRenderedP2 = "true";
        isCollapsedP2 = "false";
        isCollapsedP1 = "true";
    }

    public void onRowUnselect(UnselectEvent event) {
        isRenderedP2 = "false";
        isCollapsedP2 = "false";
        isCollapsedP1 = "false";
    }

    public void editAnnouncement() {
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();

            if (selectedAnnouncement.getHeader() != "" && selectedAnnouncement.getContext() != "" && selectedAnnouncement.getStartDate() != null && selectedAnnouncement.getEndDate() != null) {

                selectedAnnouncement.setAnnouncementId(selectedAnnouncement.getAnnouncementId());
                selectedAnnouncement.setDepartmentInfoId(selectedAnnouncement.getDepartmentInfoId());

                String citizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");
                Criteria getUserinfoCr = ses.createCriteria(Userinfo.class);
                getUserinfoCr.add(Restrictions.eq("citizenshipNumber", citizenshipNumber));
                List<Userinfo> temp = getUserinfoCr.list();
                Userinfo userinfoTemp = temp.get(0);
                selectedAnnouncement.setAuthorCitizenshipNumber(userinfoTemp);

                selectedAnnouncement.setType(selectedAnnouncement.getType());
                selectedAnnouncement.setImportance(selectedAnnouncement.getImportance());
                selectedAnnouncement.setHeader(selectedAnnouncement.getHeader());
                selectedAnnouncement.setContext(selectedAnnouncement.getContext());
                selectedAnnouncement.setStartDate(selectedAnnouncement.getStartDate());
                selectedAnnouncement.setEndDate(selectedAnnouncement.getEndDate());
                selectedAnnouncement.setIsActive(selectedAnnouncement.getIsActive());

                ses.update(selectedAnnouncement);

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement Updated", "Selected announcement has been successfully updated."));

                tx.commit();
                ses.close();

                isRenderedP2 = "false";
                isCollapsedP2 = "false";
                isCollapsedP1 = "false";

                announcementList = listAllAnnouncements();
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Update Error", "Please fill all the required fields for an announcement."));
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Announcement Update Error", "Selected announcement couldn't updated."));

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }

            e.printStackTrace();
        }
    }

    public String getIsRenderedP0() {
        return isRenderedP0;
    }

    public void setIsRenderedP0(String isRenderedP0) {
        this.isRenderedP0 = isRenderedP0;
    }

    public String getIsCollapsedP0() {
        return isCollapsedP0;
    }

    public void setIsCollapsedP0(String isCollapsedP0) {
        this.isCollapsedP0 = isCollapsedP0;
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

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
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

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public List<Departmentinfo> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Departmentinfo> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Announcementinfo> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcementinfo> announcementList) {
        this.announcementList = announcementList;
    }

    public Announcementinfo getSelectedAnnouncement() {
        return selectedAnnouncement;
    }

    public void setSelectedAnnouncement(Announcementinfo selectedAnnouncement) {
        this.selectedAnnouncement = selectedAnnouncement;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

}
