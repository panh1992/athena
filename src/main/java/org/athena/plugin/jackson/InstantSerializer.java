package org.athena.plugin.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstantSerializer extends JsonSerializer<Instant> {

    public static final InstantSerializer INSTANCE = new InstantSerializer();

    @Override
    public void serialize(Instant instant, JsonGenerator generator, SerializerProvider serializers) throws IOException {

        generator.writeString(instant.toString());

    }

}
