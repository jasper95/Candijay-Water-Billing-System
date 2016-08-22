/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.impl;

import com.dao.DataTableDao;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.service.DataTableService;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println(countFiltered);
            return new DataSet<T>(results, count, countFiltered);
        }
        else return new DataSet<T>(new ArrayList(), count, Long.valueOf(0));
        
    }
}
