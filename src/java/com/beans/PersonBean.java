/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.entity.Userinfo;
import com.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "personBean")
@ViewScoped
public class PersonBean implements Serializable {

    private Session ses;
    private Transaction tx = null;

    private String citizenshipNumberFilterValue;

    private LazyDataModel<Userinfo> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Userinfo>() {

            @Override
            public Object getRowKey(Userinfo object) {
                return object.getCitizenshipNumber();
            }

            @Override
            public List<Userinfo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                ses = HibernateUtil.getSessionFactory().openSession();
                tx = ses.beginTransaction();

                Criteria cr = ses.createCriteria(Userinfo.class);
                Criteria crGeneral = ses.createCriteria(Userinfo.class);

                /*citizenshipNumberFilter*/
                if (filters.size() > 0) {
                    for (Map.Entry<String, Object> entry : filters.entrySet()) {

                        String key = entry.getKey();
                        String value = (String) entry.getValue();

                        if (key != null && key.equals("departmentInfoId.name")) {
                            cr.createAlias("departmentInfoId", "d", JoinType.LEFT_OUTER_JOIN);
                            cr.add(Restrictions.like("d.name", value, MatchMode.ANYWHERE));
                            
                            crGeneral.createAlias("departmentInfoId", "d", JoinType.LEFT_OUTER_JOIN);
                            crGeneral.add(Restrictions.like("d.name", value, MatchMode.ANYWHERE));
                        }
                        
                        if (key != null && key.equals("citizenshipNumber")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                        }

                        if (key != null && key.equals("userType")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                        }

                        if (key != null && key.equals("isActive")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                        }

                        if (key != null && key.equals("firstName} #{lazyUser.middleName} #{lazyUser.lastName")) {
                            Criterion firstNameCr = Restrictions.like("firstName", value, MatchMode.ANYWHERE);
                            Criterion middleNameCr = Restrictions.like("middleName", value, MatchMode.ANYWHERE);
                            Criterion lastNameCr = Restrictions.like("lastName", value, MatchMode.ANYWHERE);
                            
                            Disjunction orExpCr = Restrictions.or(firstNameCr, middleNameCr, lastNameCr);
                            cr.add(orExpCr);

                            Criterion firstNameCrG = Restrictions.like("firstName", value, MatchMode.ANYWHERE);
                            Criterion middleNameCrG = Restrictions.like("middleName", value, MatchMode.ANYWHERE);
                            Criterion lastNameCrG = Restrictions.like("lastName", value, MatchMode.ANYWHERE);
                            
                            Disjunction orExpCrG = Restrictions.or(firstNameCrG, middleNameCrG, lastNameCrG);
                            crGeneral.add(orExpCrG);
                        }

                        if (key != null && key.equals("remark")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                        }

                        if (key != null && key.equals("email")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                        }
                    }
                }

                /*rowCount*/
                cr.setProjection(Projections.rowCount());
                Long rowCount = (Long) cr.uniqueResult();

                /*lazyModelRowCount*/
                lazyModel.setRowCount(rowCount.intValue());

                /*fetchData*/
                crGeneral.setFirstResult(first);
                crGeneral.setMaxResults(pageSize);
                List<Userinfo> lazyUsers = (List<Userinfo>) crGeneral.list();

                tx.commit();

                return lazyUsers;
            }

        };
    }

    public LazyDataModel<Userinfo> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Userinfo> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }

    public String getCitizenshipNumberFilterValue() {
        return citizenshipNumberFilterValue;
    }

    public void setCitizenshipNumberFilterValue(String citizenshipNumberFilterValue) {
        this.citizenshipNumberFilterValue = citizenshipNumberFilterValue;
    }

}
