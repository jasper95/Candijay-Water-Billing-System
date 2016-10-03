/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.DataTableDao;
import com.domain.*;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.service.DataTableService;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dao.util.DataTableDaoUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bert
 */
@Service("dataTableService")
public class DataTableServiceImpl implements DataTableService{

    private DataTableDao dataTableQueryHelper;
    
    @Autowired
    public DataTableServiceImpl(DataTableDao dataTableQueryHelper) {
        this.dataTableQueryHelper = dataTableQueryHelper;
    }

    @Transactional(readOnly = true)
    @Override
    public <T> DataSet <T> findWithDataTableCriterias(DatatablesCriterias criterias, Class<T> clazz){
        Long count = dataTableQueryHelper.getTotalCount(clazz);
        if( DataTableDaoUtil.hasFilteredColumn(criterias)){
            List<T> results = dataTableQueryHelper.findWithDataTablesCriteria(criterias, clazz);
            Long countFiltered = dataTableQueryHelper.getFilteredCount(criterias, clazz);
            if(!results.isEmpty() && results.get(0) instanceof MeterReading){
                for(T a: results){
                    MeterReading reading = (MeterReading) a;
                    Hibernate.initialize(reading.getAccount());
                    Hibernate.initialize(reading.getInvoice());
                }
            } else if(!results.isEmpty() && results.get(0) instanceof Invoice){
                for(T a: results){
                    Invoice invoice = (Invoice) a;
                    Hibernate.initialize(invoice.getAccount());
                    Hibernate.initialize(invoice.getPayment());
                }
            } else if(!results.isEmpty() && results.get(0) instanceof Payment){
                for(T a: results){
                    Payment payment = (Payment) a;
                    Hibernate.initialize(payment.getAccount());
                    Hibernate.initialize(payment.getInvoice());
                }
            } else if(!results.isEmpty() && results.get(0) instanceof ModifiedReading){
                for (T a: results){
                    ModifiedReading modifiedReading = (ModifiedReading) a;
                    Hibernate.initialize(modifiedReading.getReading());
                    //Hibernate.initialize(modifiedReading.getReading().getAccount());
                }
            }
            System.out.println("heree heree");
            return new DataSet<T>(results, count, countFiltered);
        }
        else return new DataSet<T>(new ArrayList(), count, Long.valueOf(0));
    }
}
