package com.ab.cart.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.money.Money;

import java.io.IOException;

public class MoneySerializer extends StdScalarSerializer<Money> {

    public MoneySerializer() { super(Money.class); }

    @Override
    public void serialize(Money value,
                          JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("amount",value.getAmount());
        jgen.writeStringField("currency",value.getCurrencyUnit().getCurrencyCode());
        jgen.writeEndObject();
    }
}
