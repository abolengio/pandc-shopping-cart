package com.ab.cart.rest.json;

import com.ab.cart.domain.EffectivePriceProduct;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;

public class EffectivePriceProductSerializer extends StdScalarSerializer<EffectivePriceProduct> {

    public EffectivePriceProductSerializer() { super(EffectivePriceProduct.class); }

    @Override
    public void serialize(EffectivePriceProduct value,
                          JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getName());
        jgen.writeObjectField("price", value.getPrice());
        jgen.writeObjectField("effectivePrice", value.getEffectivePrice());
        if(value.getRebateTimeFrame() != null) {
            jgen.writeObjectField("rebateTimeFrame", value.getRebateTimeFrame());
        }
        jgen.writeEndObject();
    }
}
