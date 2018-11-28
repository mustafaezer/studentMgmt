/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.pojos.Departmentinfo;
import com.pojos.Userinfo;
import com.util.HibernateUtil;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Turktrust Stajyer
 */
@ManagedBean
@ViewScoped
public class PersonalInfoBean {

    private Session ses;
    private Userinfo _user;
    private List<Userinfo> _userList;

    private LoginBean loginBean;

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

    private String newPassword;
    private String confirmPassword;
    private String dbPassword;

    private Date dateOfBirth;
    private String remark;
    private String isActive;
    private Integer gradingId;

    private String isCollapsedP1;
    private String isCollapsedP2;

    /**
     * Creates a new instance of PersonalInfo
     */
//    
    @PostConstruct
    public void init() {
        isCollapsedP1 = "false";
        isCollapsedP2 = "false";

        ses = HibernateUtil.getSessionFactory().openSession();
    }

    @PreDestroy
    public void destroy() {
        ses.close();
    }

    public PersonalInfoBean() {
    }

    public List<Userinfo> fetchPersonalInfo() {
        _userList = new ArrayList<>();
        ses.beginTransaction();
        String citizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

        Criteria getPerson = ses.createCriteria(Userinfo.class);
        getPerson.add(Restrictions.eq("citizenshipNumber", citizenshipNumber));
        _userList = getPerson.list();

        ses.getTransaction().commit();

        return _userList;
    }

    public void changePassword() {
        try {
            ses.beginTransaction();

            String passwordCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");

            Criteria getPasswordFromDB = ses.createCriteria(Userinfo.class);
            getPasswordFromDB.add(Restrictions.eq("citizenshipNumber", passwordCitizenshipNumber));
            List<Userinfo> tempListToHoldUserObject = getPasswordFromDB.list();
            dbPassword = tempListToHoldUserObject.get(0).getPassword();
            ses.getTransaction().commit();

            if (UserBean.getSHA256(password).equals(dbPassword)) {
                if (newPassword.equals(confirmPassword)) {
                    ses.beginTransaction();

                    String sessionCitizenshipNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("citizenshipNumber");
                    Criteria updatePassword = ses.createCriteria(Userinfo.class);
                    updatePassword.add(Restrictions.eq("citizenshipNumber", sessionCitizenshipNumber));
                    List<Userinfo> tempListToHoldUser = updatePassword.list();
                    Userinfo userToUpdate = new Userinfo();
                    userToUpdate = tempListToHoldUser.get(0);
                    userToUpdate.setPassword(UserBean.getSHA256(newPassword));
                    ses.saveOrUpdate(userToUpdate);
                    ses.getTransaction().commit();

                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Updated", "Your password updated successfully."));
                } else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to update password.", "Your passwords do not match."));
                }
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to update password.", "Your current password is incorrect."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return departmentinfo;
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

    public Integer getGradingId() {
        return gradingId;
    }

    public void setGradingId(Integer gradingId) {
        this.gradingId = gradingId;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

}
