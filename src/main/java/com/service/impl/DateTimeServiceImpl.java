package com.service.impl;

import com.service.DateTimeService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;


/**
 * Created by jasper on 8/2/16.
 */
@Service("dateTimeService")
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public DateTime getCurrentDateAndTime() {
        return new DateTime(DateTimeZone.forID("Asia/Manila"));
    }
}
