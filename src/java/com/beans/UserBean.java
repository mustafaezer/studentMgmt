/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Departmentinfo;
import com.entity.Userinfo;
import com.util.HibernateUtil;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

    private Session ses;
    private Userinfo selectedUser;

    private List<Userinfo> userList;
    private List<Departmentinfo> departmentList;

    private String citizenshipNumber;
    private String userType;
    private Departmentinfo departmentinfo;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String number;
    private String email;
    private String password;
    private Date dateOfBirth;
    private String remark;
    private String isActive;

    private String isCollapsedP1;
    private String isCollapsedP2;
    private String isCollapsedP3;

    private String isRenderedP1;
    private String isRenderedP2;
    private String isRenderedP3;
    private String isRenderedEditUserButton = "false";

    Transaction tx = null;

    public UserBean() {
    }

    @PostConstruct
    public void init() {
        isRenderedP1 = "true";
        isRenderedP2 = "false";
        isRenderedP3 = "false";

        isCollapsedP1 = "false";
        isCollapsedP2 = "true";
        isCollapsedP3 = "true";

        listAllUsers();
        listAllDepartments();
    }

    public List<Userinfo> listAllUsers() {
        userList = new ArrayList<>();
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            Criteria cr = ses.createCriteria(Userinfo.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.addOrder(Order.asc("isActive"));
            cr.addOrder(Order.asc("departmentInfoId.departmentInfoId"));
            cr.addOrder(Order.asc("userType"));
            cr.addOrder(Order.asc("firstName"));
            cr.addOrder(Order.asc("middleName"));
            cr.addOrder(Order.asc("lastName"));
            cr.addOrder(Order.asc("citizenshipNumber"));
            cr.addOrder(Order.asc("gender"));

            userList = cr.list();
            tx.commit();
            ses.close();

        } catch (Exception e) {
            if (ses != null && ses.isOpen()) {
                ses.close();
                ses = null;
            }
            
            e.printStackTrace();
        }
        return userList;
    }

    public void createNewUserPanelInitializer() {
        isCollapsedP2 = "false";
        isCollapsedP1 = "true";
        isCollapsedP3 = "true";

        isRenderedP2 = "true";
        isRenderedP1 = "true";
        isRenderedP3 = "false";
    }

    public void createNewUser() {
        try {
            if (citizenshipNumber != "" && userType != "" && departmentinfo != null && firstName != "" && lastName != "" && gender != "" && email != "" && password != "" && dateOfBirth != null && isActive != "") {
                ses = HibernateUtil.getSessionFactory().openSession();
                tx = ses.beginTransaction();
                Userinfo newUser = new Userinfo();
                newUser.setCitizenshipNumber(citizenshipNumber);
                newUser.setUserType(userType);
                newUser.setDepartmentInfoId(departmentinfo);
                newUser.setFirstName(firstName);
                newUser.setMiddleName(middleName);
                newUser.setLastName(lastName);
                newUser.setGender(gender);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setDateOfBirth(dateOfBirth);
                newUser.setRemark(remark);
                newUser.setIsActive(isActive);
                ses.save(newUser);
                tx.commit();
                ses.close();

                isCollapsedP1 = "false";
                isCollapsedP2 = "true";
                isCollapsedP3 = "true";

                isRenderedP1 = "true";
                isRenderedP2 = "false";
                isRenderedP3 = "false";

                listAllUsers();

                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Creation Successful", "New user has been successfully created with the informations given."));
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Creation Error", "Please fill the all required fields for user creation."));
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

    public void updateUserPanelInitializer() {
        if (selectedUser == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Edit User Information", "Please select a user first to proceed this event."));
        } else {
            isCollapsedP3 = "false";
            isCollapsedP1 = "true";
            isCollapsedP2 = "true";

            isRenderedP3 = "true";
            isRenderedP1 = "true";
            isRenderedP2 = "false";
        }
    }

    public void updateUser() {
        try {
            ses = HibernateUtil.getSessionFactory().openSession();

            tx = ses.beginTransaction();
            selectedUser.setFirstName(selectedUser.getFirstName());
            selectedUser.setMiddleName(selectedUser.getMiddleName());
            selectedUser.setLastName(selectedUser.getLastName());
            selectedUser.setGender(selectedUser.getGender());
            selectedUser.setEmail(selectedUser.getEmail());
            selectedUser.setDateOfBirth(selectedUser.getDateOfBirth());
            selectedUser.setRemark(selectedUser.getRemark());
            selectedUser.setIsActive(selectedUser.getIsActive());
            ses.update(selectedUser);
            tx.commit();
            ses.close();

            isCollapsedP1 = "false";
            isCollapsedP2 = "true";
            isCollapsedP3 = "true";

            isRenderedP1 = "true";
            isRenderedP2 = "false";
            isRenderedP3 = "false";

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful", "You have successfully updated informations of user with citizenship number: " + selectedUser.getCitizenshipNumber()));
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

    public void cancel() {
        isCollapsedP1 = "false";
        isCollapsedP2 = "true";
        isCollapsedP3 = "true";

        isRenderedP1 = "true";
        isRenderedP2 = "false";
        isRenderedP3 = "false";
    }

    public static String getSHA256(String data) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
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
        isRenderedEditUserButton = "true";
    }

    public void onRowUnselect(UnselectEvent event) {
        isRenderedEditUserButton = "false";
    }

    public Userinfo getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Userinfo selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        this.password = getSHA256(password);
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

    public List<Userinfo> getUserList() {
        return userList;
    }

    public void setUserList(List<Userinfo> userList) {
        this.userList = userList;
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

    public List<Departmentinfo> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Departmentinfo> departmentList) {
        this.departmentList = departmentList;
    }

    public String getIsRenderedEditUserButton() {
        return isRenderedEditUserButton;
    }

    public void setIsRenderedEditUserButton(String isRenderedEditUserButton) {
        this.isRenderedEditUserButton = isRenderedEditUserButton;
    }

}
