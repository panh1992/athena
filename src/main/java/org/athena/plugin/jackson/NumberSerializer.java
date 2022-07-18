package org.athena.plugin.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberSerializer extends JsonSerializer<Number> {

    public static final NumberSerializer INSTANCE = new NumberSerializer();

    @Override
    public void serialize(Number number, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (number instanceof Long) {
            generator.writeString(BigInteger.valueOf(number.longValue()).toString());
        } else {
            generator.writeString(BigDecimal.valueOf(number.doubleValue()).toPlainString());
        }
    }

}
