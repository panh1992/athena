package org.athena.plugin.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstantDeserializer extends JsonDeserializer<Instant> {

    public static final InstantDeserializer INSTANCE = new InstantDeserializer();

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {

        if (jsonParser != null && !Strings.isNullOrEmpty(jsonParser.getText())) {
            return Instant.parse(jsonParser.getText());
        } else {
            return null;
        }

    }

}
