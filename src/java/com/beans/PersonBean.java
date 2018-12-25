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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

                        if (key != null && key.equals("citizenshipNumber")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like("citizenshipNumber", value, MatchMode.ANYWHERE));
                        }
                        
                        if (key != null && key.equals("remark")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like("remark", value, MatchMode.ANYWHERE));
                        }
                        
                        if (key != null && key.equals("email")) {
                            cr.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                            crGeneral.add(Restrictions.like("email", value, MatchMode.ANYWHERE));
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
