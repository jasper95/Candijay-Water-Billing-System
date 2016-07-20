/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.util;

import com.dao.DataTableDao;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bert
 */
@Repository("dataTableQueryHelper")
public class DataTableQueryHelper implements DataTableDao{
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public <T>List<T> findWithDataTablesCriteria(DatatablesCriterias criterias, Class<T> clazz){
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM "+ clazz.getName()+" p");
        queryBuilder.append(DataTableDaoUtil.getFilterQuery(criterias));
        if (criterias.hasOneSortedColumn()) {
            List<String> orderParams = new ArrayList<String>();
            queryBuilder.append(" ORDER BY ");
            for (ColumnDef columnDef : criterias.getSortingColumnDefs()) {
                System.out.println(columnDef.getName());    
                orderParams.add("p." + columnDef.getName() + " " + columnDef.getSortDirection());
            }
            Iterator<String> itr2 = orderParams.iterator();
            while (itr2.hasNext()) {
                queryBuilder.append(itr2.next());
                if (itr2.hasNext()) {
                        queryBuilder.append(" , ");
                }
            }
        }
        System.out.println(queryBuilder.toString());
        TypedQuery<T> query = entityManager.createQuery(queryBuilder.toString(), clazz);
        query.setFirstResult(criterias.getStart());
        query.setMaxResults(criterias.getLength());
        return query.getResultList();
    }

    @Override
    public Long getFilteredCount(DatatablesCriterias criterias, Class clazz) {
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM "+ clazz.getName()+" p");
        queryBuilder.append(DataTableDaoUtil.getFilterQuery(criterias));
        Query query = entityManager.createQuery(queryBuilder.toString());
        return Long.parseLong(String.valueOf(query.getResultList().size()));
    }

    @Override
    public Long getTotalCount(Class clazz) {
        Query query = entityManager.createQuery("SELECT COUNT(p) FROM "+clazz.getName()+" p");
        return (Long) query.getSingleResult();
    }
}