package com.dao.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by jasper on 8/3/16.
 */
public class AuditorDateTimeSerializer extends JsonSerializer<DateTime> {
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, ''yy hh:mm:ss a");
    @Override
    public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(formatter.print(value));

    }
}
