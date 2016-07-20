package com.service.impl;

import com.dao.springdatajpa.AddressRepository;
import com.domain.Address;
import com.service.FormOptionsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

import org.joda.time.LocalDate;
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

    @Override
    public HashMap<String, Collections> getCustomerFormOptions() {
        HashMap<Character, String> genderOptions = new HashMap();
        Set<String> brgyList = new HashSet(), zoneList = new HashSet();
        genderOptions.put('M', "Male");
        genderOptions.put('F', "Female");
        for( Address address : addressRepo.findAll()){
            brgyList.add(address.getBrgy());
            zoneList.add(address.getLocationCode().toString());
        }
        HashMap allOptions = new HashMap();
        allOptions.put("brgy", brgyList);
        allOptions.put("gender", genderOptions);
        allOptions.put("zone", zoneList);
        return allOptions;
    }

    @Override
    public HashMap<String, Collections> getMeterReadingFormOptions() {
        Map monthOptions = new HashMap(), yearOptions = new HashMap();
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
}
