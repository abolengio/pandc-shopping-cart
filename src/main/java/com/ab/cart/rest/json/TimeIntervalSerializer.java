package com.ab.cart.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class TimeIntervalSerializer extends StdScalarSerializer<ReadableInterval> {

    private static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

    public TimeIntervalSerializer() { super(ReadableInterval.class); }

    @Override
    public void serialize(ReadableInterval value,
                          JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("start", fmt.print(value.getStart()));
        jgen.writeStringField("end", fmt.print(value.getEnd()));
        jgen.writeEndObject();
    }
}
