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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "personBean")
@ViewScoped
public class PersonBean implements Serializable {

    private Session ses;

    private LazyDataModel<Userinfo> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Userinfo>() {
            @Override
            public List<Userinfo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                /**
                 * Row Count 
                 * ****************************************************
                 */
                ses = HibernateUtil.getSessionFactory().openSession();
                Criteria cr = ses.createCriteria(Userinfo.class);
                cr.setProjection(Projections.rowCount());
                Long rowCount = (Long) cr.uniqueResult();
                lazyModel.setRowCount(rowCount.intValue());

                /**
                 * Filter 
                 * ****************************************************
                 */
                String queryStmt = "from Userinfo";
                if (filters.size() > 0) {
                    String whereHql = " where ";

                    for (Map.Entry<String, Object> entry : filters.entrySet()) {
                        String key = entry.getKey();
                        String value = (String) entry.getValue();

                        whereHql += " " + key + " like '" + value + "%'";
                    }
                    queryStmt += whereHql;
                }

                /**
                 * Fetch Data 
                 * ****************************************************
                 */
                Query query = ses.createQuery(queryStmt);
                query.setFirstResult(first);
                query.setMaxResults(pageSize);
                List<Userinfo> sonuclar = (List<Userinfo>) query.list();

                return sonuclar;
            }

            @Override
            public Object getRowKey(Userinfo object) {
                return object.getCitizenshipNumber();
            }
        };
    }

    public LazyDataModel<Userinfo> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Userinfo> lazyModel) {
        this.lazyModel = lazyModel;
    }
}
