/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.util;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Bert
 */
public class DataTableDaoUtil{
    
    public static StringBuilder getFilterQuery(DatatablesCriterias criterias){
		StringBuilder queryBuilder = new StringBuilder();
		List<String> paramList = new ArrayList<String>();
		
		/**
		 * Step 1.1: global filtering
		 */
		if (StringUtils.isNotBlank(criterias.getSearch()) && criterias.hasOneFilterableColumn()) {
                        queryBuilder.append(" WHERE ");

			for (ColumnDef columnDef : criterias.getColumnDefs()) {
				if (columnDef.isFilterable() && StringUtils.isBlank(columnDef.getSearch())) {
					paramList.add(" LOWER(p." + columnDef.getName()
							+ ") LIKE '%?%'".replace("?", criterias.getSearch().toLowerCase()));
				}
			}

			Iterator<String> itr = paramList.iterator();
			while (itr.hasNext()) {
				queryBuilder.append(itr.next());
				if (itr.hasNext()) {
					queryBuilder.append(" OR ");
				}
			}
		}

		/**
		 * Step 1.2: individual column filtering
		 */
		if (hasFilteredColumn(criterias)) {
			paramList = new ArrayList<>();
			if(!queryBuilder.toString().contains("WHERE")){
				queryBuilder.append(" WHERE ");
			}
			else{
				queryBuilder.append(" AND ");
			}

			for (ColumnDef columnDef : criterias.getColumnDefs()) {
				if (columnDef.isFilterable()){
					if (StringUtils.isNotBlank(columnDef.getSearchFrom())) {
						if (columnDef.getName().equalsIgnoreCase("birthDate")) {
							paramList.add("p." + columnDef.getName() + " >= '" + columnDef.getSearchFrom() + "'");
						}
						else {
							paramList.add("p." + columnDef.getName() + " >= " + columnDef.getSearchFrom());
						}
					}

					if (StringUtils.isNotBlank(columnDef.getSearchTo())) {
						if (columnDef.getName().equalsIgnoreCase("birthDate")) {
							paramList.add("p." + columnDef.getName() + " < '" + columnDef.getSearchTo() + "'");
						}
						else {
							paramList.add("p." + columnDef.getName() + " < " + columnDef.getSearchTo());
						}
					}
					
					if(StringUtils.isNotBlank(columnDef.getSearch())) {
						String[] q = columnDef.getName().split("\\.");
						if(q[q.length-1].equalsIgnoreCase("date")){
							String[] ins = columnDef.getSearch().split("-yadcf_delim-");
							if(ins.length > 0 && !ins[0].isEmpty()){
								paramList.add("p." + columnDef.getName() + " >= '" + ins[0] + "'");
							}
							if(ins.length > 1 && !ins[1].isEmpty()){
								paramList.add("p." + columnDef.getName() + " < '" + ins[1] + "'");
							}
						}
						else if (q[q.length-1].equalsIgnoreCase("month") || q[q.length-1].equalsIgnoreCase("year") || q[q.length-1].equalsIgnoreCase("id") || q[q.length-1].equalsIgnoreCase("status")){
							paramList.add(" LOWER(p." + columnDef.getName()
											+ ") = '?'".replace("?", columnDef.getSearch().toLowerCase()));
						}
						else if(q[q.length-1].equalsIgnoreCase("number")){
							String[] query = columnDef.getSearch().toLowerCase().split("-");
							String newQuery;
							try {
								newQuery = (query.length > 1) ? query[0] + "-"+ String.format("%05d", Integer.valueOf(query[1])) : query[0];
							}catch(NumberFormatException e){
								newQuery = query[0];
							}
							paramList.add(" LOWER(p." + columnDef.getName()
									+ ") LIKE '%?%'".replace("?", newQuery));
						}
						else {
							paramList.add(" LOWER(p." + columnDef.getName()
											+ ") LIKE '%?%'".replace("?", columnDef.getSearch().toLowerCase()));
						}
					}
				}
			}
			Iterator<String> itr = paramList.iterator();
			while (itr.hasNext()) {
				queryBuilder.append(itr.next());
				if (itr.hasNext()) {
					queryBuilder.append(" AND ");
				}
			}
		}
		return queryBuilder;
	}
	public static boolean hasFilteredColumn(DatatablesCriterias criterias){
		for(ColumnDef column: criterias.getColumnDefs()) {
			if (column.isFiltered()) {
				String[] q = column.getName().split("\\.");
				if (q[q.length - 1].equalsIgnoreCase("date")) {
					String[] ins = column.getSearch().split("-yadcf_delim-");
					if (ins.length > 0) {
						return true;
					}
				} else return true;
			}
		}
		return false;
	}
}
