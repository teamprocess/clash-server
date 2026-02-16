package com.process.clash.infrastructure.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter KST_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantKstSerializerCustomizer() {
        return builder -> builder.serializerByType(
            Instant.class,
            new StdScalarSerializer<>(Instant.class) {
                @Override
                public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                    gen.writeString(KST_FORMATTER.format(value.atZone(KST_ZONE_ID)));
                }
            }
        );
    }
}
