/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Departmentinfo;
import com.entity.Subjectinfo;
import com.entity.Userinfo;
import com.util.HibernateUtil;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import org.hibernate.Session;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
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
public class DepartmentBean {

    private Session ses;

    private Departmentinfo departmentinfo;
    private String name;

    private Departmentinfo selectedDepartment;

    private List<Departmentinfo> departmentList;
    private List<Userinfo> instructorList;
    private List<Userinfo> studentList;
    private List<Subjectinfo> subjectList;

    private String citizenshipNumber;

    private String isRenderedP0;
    private String isRenderedP1;
    private String isRenderedP2;
    private String isRenderedP3;
    private String isRenderedP4;
    private String isRenderedCommandButton = "false";

    private String isCollapsedP0;
    private String isCollapsedP1;
    private String isCollapsedP2;
    private String isCollapsedP3;
    private String isCollapsedP4;

    @PostConstruct
    public void init() {
        ses = HibernateUtil.getSessionFactory().openSession();

        isRenderedP0 = "true";
        isRenderedP1 = "true";
        isRenderedP2 = "false";
        isRenderedP3 = "false";
        isRenderedP4 = "false";

        isCollapsedP0 = "false";
        isCollapsedP1 = "false";
        isCollapsedP2 = "true";
        isCollapsedP3 = "true";
        isCollapsedP4 = "true";

        listAllDepartments();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public void createNewDepartment() {
        if (name != "") {
            try {
                ses.beginTransaction();

                Departmentinfo newDepartment = new Departmentinfo();
                newDepartment.setName(name);
                ses.save(newDepartment);
                ses.getTransaction().commit();

                isCollapsedP0 = "true";

                listAllDepartments();

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Department Created", "Department has been successfully created: " + newDepartment.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Department Creation Error", "Please enter a valid department name to proceed this event."));
        }
    }

    public List<Departmentinfo> listAllDepartments() {
        departmentList = new ArrayList<>();

        try {
            ses.beginTransaction();
            Criteria cr = ses.createCriteria(Departmentinfo.class);
            cr.addOrder(Order.asc("name"));
            departmentList = cr.list();
            ses.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return departmentList;
    }

    public List<Userinfo> listDepartmentInstructors() {
        instructorList = new ArrayList<>();

        if (selectedDepartment == null) {
            isRenderedP0 = "true";
            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "false";
            isRenderedP4 = "false";

            isCollapsedP0 = "false";
            isCollapsedP1 = "false";
            isCollapsedP2 = "true";
            isCollapsedP3 = "true";
            isCollapsedP4 = "true";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Instructor Members", "Please select a department first to proceed this event."));
        } else {
            try {
                ses.beginTransaction();
                String type = "Instructor";
                Criteria cr = ses.createCriteria(Userinfo.class);
                cr.add(Restrictions.eq("departmentInfoId.departmentInfoId", selectedDepartment.getDepartmentInfoId()));
                cr.add(Restrictions.eq("userType", type));
                cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                instructorList = cr.list();
                ses.getTransaction().commit();

                if (instructorList.size() > 0) {
                    isRenderedP2 = "true";
                    isCollapsedP2 = "false";

                    isRenderedP0 = "true";
                    isCollapsedP0 = "true";

                    isRenderedP3 = "false";
                    isCollapsedP3 = "true";
                    isRenderedP4 = "false";
                    isCollapsedP4 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Show Instructor Members", "You are successfully viewing instructor members of " + selectedDepartment.getName() + " department."));
                } else {
                    isRenderedP0 = "true";
                    isRenderedP1 = "true";
                    isRenderedP2 = "false";
                    isRenderedP3 = "false";
                    isRenderedP4 = "false";

                    isCollapsedP0 = "false";
                    isCollapsedP1 = "false";
                    isCollapsedP2 = "true";
                    isCollapsedP3 = "true";
                    isCollapsedP4 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Instructor Members", "There are not any instructor members of " + selectedDepartment.getName() + " yet."));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instructorList;
    }

    public List<Userinfo> listDepartmentStudents() {

        studentList = new ArrayList<>();
        if (selectedDepartment == null) {
            isRenderedP0 = "true";
            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "false";
            isRenderedP4 = "false";

            isCollapsedP0 = "false";
            isCollapsedP1 = "false";
            isCollapsedP2 = "true";
            isCollapsedP3 = "true";
            isCollapsedP4 = "true";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Instructor Members", "Please select a department first to proceed this event."));
        } else {
            try {
                ses.beginTransaction();
                String type = "Student";
                Criteria cr = ses.createCriteria(Userinfo.class);
                cr.add(Restrictions.eq("departmentInfoId.departmentInfoId", selectedDepartment.getDepartmentInfoId()));
                cr.add(Restrictions.eq("userType", type));
                cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                studentList = cr.list();
                ses.getTransaction().commit();

                if (studentList.size() > 0) {
                    isRenderedP3 = "true";
                    isCollapsedP3 = "false";

                    isRenderedP0 = "true";
                    isCollapsedP0 = "true";

                    isRenderedP2 = "false";
                    isCollapsedP2 = "true";
                    isRenderedP4 = "false";
                    isCollapsedP4 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Show Student Members", "You are successfully viewing student members of " + selectedDepartment.getName() + " department."));
                } else {
                    isRenderedP0 = "true";
                    isRenderedP1 = "true";
                    isRenderedP2 = "false";
                    isRenderedP3 = "false";
                    isRenderedP4 = "false";

                    isCollapsedP0 = "false";
                    isCollapsedP1 = "false";
                    isCollapsedP2 = "true";
                    isCollapsedP3 = "true";
                    isCollapsedP4 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Student Members", "There are not any student members of " + selectedDepartment.getName() + " yet."));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return studentList;
    }

    public List<Subjectinfo> listDepartmentSubjects() {

        subjectList = new ArrayList<>();
        if (selectedDepartment == null) {
            isRenderedP0 = "true";
            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "false";
            isRenderedP4 = "false";

            isCollapsedP0 = "false";
            isCollapsedP1 = "false";
            isCollapsedP2 = "true";
            isCollapsedP3 = "true";
            isCollapsedP4 = "true";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Subjects Given By Department", "Please select a department first to proceed this event."));
        } else {
            try {
                ses.beginTransaction();
                Criteria cr = ses.createCriteria(Subjectinfo.class);
                cr.add(Restrictions.eq("departmentInfoId.departmentInfoId", selectedDepartment.getDepartmentInfoId()));
                cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                subjectList = cr.list();
                ses.getTransaction().commit();

                if (subjectList.size() > 0) {
                    isRenderedP4 = "true";
                    isCollapsedP4 = "false";

                    isRenderedP0 = "true";
                    isCollapsedP0 = "true";

                    isRenderedP2 = "false";
                    isCollapsedP2 = "true";
                    isRenderedP3 = "false";
                    isCollapsedP3 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Show Subjects Given By Department", "You are successfully viewing subjects given by " + selectedDepartment.getName() + " department."));
                } else {
                    isRenderedP0 = "true";
                    isRenderedP1 = "true";
                    isRenderedP2 = "false";
                    isRenderedP3 = "false";
                    isRenderedP4 = "false";

                    isCollapsedP0 = "false";
                    isCollapsedP1 = "false";
                    isCollapsedP2 = "true";
                    isCollapsedP3 = "true";
                    isCollapsedP4 = "true";

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Show Subjects Given By Department", "There are not any subjects given by " + selectedDepartment.getName() + " yet."));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subjectList;
    }

    public void onRowSelect(SelectEvent event) {
        isRenderedCommandButton = "true";
    }

    public void onRowUnselect(UnselectEvent event) {
        isRenderedCommandButton = "false";
        isCollapsedP0 = "false";
        isRenderedP2 = "false";
        isRenderedP3 = "false";
        isRenderedP4 = "false";
    }

    public DepartmentBean() {
    }

    public List<Departmentinfo> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Departmentinfo> departmentList) {
        this.departmentList = departmentList;
    }

    public Departmentinfo getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Departmentinfo selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public Departmentinfo getDepartmentinfo() {
        return departmentinfo;
    }

    public void setDepartmentinfo(Departmentinfo departmentinfo) {
        this.departmentinfo = departmentinfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getIsRenderedP0() {
        return isRenderedP0;
    }

    public void setIsRenderedP0(String isRenderedP0) {
        this.isRenderedP0 = isRenderedP0;
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

    public String getIsRenderedP3() {
        return isRenderedP3;
    }

    public void setIsRenderedP3(String isRenderedP3) {
        this.isRenderedP3 = isRenderedP3;
    }

    public String getIsRenderedP4() {
        return isRenderedP4;
    }

    public void setIsRenderedP4(String isRenderedP4) {
        this.isRenderedP4 = isRenderedP4;
    }

    public String getIsCollapsedP0() {
        return isCollapsedP0;
    }

    public void setIsCollapsedP0(String isCollapsedP0) {
        this.isCollapsedP0 = isCollapsedP0;
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

    public String getIsCollapsedP3() {
        return isCollapsedP3;
    }

    public void setIsCollapsedP3(String isCollapsedP3) {
        this.isCollapsedP3 = isCollapsedP3;
    }

    public String getIsCollapsedP4() {
        return isCollapsedP4;
    }

    public void setIsCollapsedP4(String isCollapsedP4) {
        this.isCollapsedP4 = isCollapsedP4;
    }

    public List<Userinfo> getInstructorList() {
        return instructorList;
    }

    public void setInstructorList(List<Userinfo> instructorList) {
        this.instructorList = instructorList;
    }

    public List<Userinfo> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Userinfo> studentList) {
        this.studentList = studentList;
    }

    public List<Subjectinfo> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subjectinfo> subjectList) {
        this.subjectList = subjectList;
    }

    public String getIsRenderedCommandButton() {
        return isRenderedCommandButton;
    }

    public void setIsRenderedCommandButton(String isRenderedCommandButton) {
        this.isRenderedCommandButton = isRenderedCommandButton;
    }

}
