/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 *
 * @author Bert
 */
public interface DataTableService {
    public <T> DataSet <T> findWithDataTableCriterias(DatatablesCriterias criterias, Class<T> clazz);
}
