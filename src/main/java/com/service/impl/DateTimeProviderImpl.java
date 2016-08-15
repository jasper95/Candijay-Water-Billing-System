package com.service.impl;

import com.service.DateTimeService;
import org.springframework.data.auditing.DateTimeProvider;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jasper on 8/2/16.
 */
public class DateTimeProviderImpl implements DateTimeProvider {

    private DateTimeService dateTimeService;

    public DateTimeProviderImpl(DateTimeService dateTimeService){
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Calendar getNow() {
        return dateTimeService.getCurrentDateAndTime().toCalendar(Locale.getDefault());
    }
}