package com.service.impl;

import com.dao.springdatajpa.AddressRepository;
import com.domain.Address;
import com.service.FormOptionsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bert on 7/17/2016.
 */
@Service
public class FormOptionsServiceImpl implements FormOptionsService{

    private AddressRepository addressRepo;

    @Autowired
    public FormOptionsServiceImpl(AddressRepository addressRepo){
        this.addressRepo = addressRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public HashMap<String, Collections> getCustomerFormOptions() {
        HashMap<Character, String> genderOptions = new HashMap();
        SortedSet<String> brgyList = new TreeSet();
        HashMap<Integer, Integer> purokOptions =  new HashMap();
        genderOptions.put('M', "Male");
        genderOptions.put('F', "Female");
        for(int i=1; i<=7; i++)
            purokOptions.put(i, i);
        for( Address address : addressRepo.findAllByOrderByBrgyAsc())
            brgyList.add(address.getBrgy());
        HashMap allOptions = new HashMap();
        allOptions.put("brgy", brgyList);
        allOptions.put("gender", genderOptions);
        allOptions.put("purok", purokOptions);
        return allOptions;
    }

    @Override
    public HashMap<String, Collections> getMeterReadingFormOptions() {
        Map monthOptions = new HashMap(), yearOptions = new LinkedHashMap();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1; i<=12; i++){
            LocalDate date = new LocalDate(2010, i, 1);
            monthOptions.put(i, date.toString("MMM"));
        }
        for(int i = year; i > year-7; i--)
            yearOptions.put(i, i);
        HashMap allOptions = new HashMap();
        allOptions.put("month", monthOptions);
        allOptions.put("year", yearOptions);
        return allOptions;
    }

    @Transactional(readOnly = true)
    @Override
    public HashMap<String, Collections> getReportFormOptions() {
        Map monthOptions = new HashMap(),  yearOptions = new LinkedHashMap(), typeOptionsReport = new HashMap(), typeOptionsChart = new HashMap(), typeAcctblty = new HashMap();
        Set<String> zoneList = new HashSet();
        SortedSet<String> brgyList = new TreeSet();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for( Address address : addressRepo.findAllByOrderByBrgyAsc()){
            brgyList.add(address.getBrgy());
            zoneList.add(address.getLocationCode().toString());
        }
        for(int i=1; i<=12; i++){
            LocalDate date = new LocalDate(2010, i, 1);
            monthOptions.put(i, date.toString("MMMM"));
        }
        for(int i = year; i > year-7; i--)
            yearOptions.put(i, i);
        typeOptionsReport.put("1", "Collectibles");
        typeOptionsReport.put("2", "Collection");
        typeOptionsChart.put("1", "Water Consumption");
        typeOptionsChart.put("2", "Collection, Collectibles and Expenses");
        typeAcctblty.put("1", "Bills");
        typeAcctblty.put("2", "Notice of Disconnection");
        HashMap allOptions = new HashMap();
        allOptions.put("month", monthOptions);
        allOptions.put("year", yearOptions);
        allOptions.put("brgy", brgyList);
        allOptions.put("zone", zoneList);
        allOptions.put("typeReport", typeOptionsReport);
        allOptions.put("typeChart", typeOptionsChart);
        allOptions.put("acctblty", typeAcctblty);
        return allOptions;
    }

    @Override
    public HashMap<String, Collections> getExpenseFormOptions() {
        Map monthOptions = new HashMap(),  yearOptions = new LinkedHashMap(), typeOptionsExpense = new HashMap();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1; i<=12; i++){
            LocalDate date = new LocalDate(2010, i, 1);
            monthOptions.put(i, date.toString("MMMM"));
        }
        for(int i = year; i > year-7; i--)
            yearOptions.put(i, i);
        typeOptionsExpense.put("1", "Wage(1-15)");
        typeOptionsExpense.put("2", "Wage(16-30)");
        typeOptionsExpense.put("3", "Power Usage");
        HashMap allOptions = new HashMap();
        allOptions.put("month", monthOptions);
        allOptions.put("year", yearOptions);
        allOptions.put("typeExpense", typeOptionsExpense);
        return allOptions;
    }
}
