/*
 * MIT License
 *
 * Copyright (c) 2020 CloudSen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.projecthephaestus.ezcloud.autoconfigure.serializer;

import io.github.projecthephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 序列化设置
 *
 * @author CloudS3n
 * @date 2021-06-17 19:09
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = AutoConfigConstants.CONFIG_PREFIX, name = "enable-serializer", havingValue = AutoConfigConstants.TRUE)
public class SerializerAutoConfiguration {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_WITH_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String STR_T = "'T'";

    static {
        log.info(AutoConfigConstants.LOADING_SERIALIZER_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer())
            .deserializerByType(LocalDate.class, new LocalDateDeserializer())
            .deserializerByType(Long.class, new LongDeserializer())
            .serializerByType(Long.class, new LongSerializer());
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    public static class LongDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return jsonParser.getValueAsLong();
        }
    }

    public static class LongSerializer extends JsonSerializer<Long> {

        @Override
        public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(String.valueOf(aLong));
        }
    }

    /**
     * 反序列化LocalDateTime
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            }
            String strTime = p.getValueAsString();
            if (StringUtils.isNotBlank(strTime) && strTime.contains(STR_T)) {
                return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(DATE_TIME_WITH_T_FORMAT));
            } else {
                return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            }
        }
    }

    /**
     * 反序列化LocalDate
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            }
            String strDate = p.getValueAsString();
            if (StringUtils.isNotBlank(strDate)) {
                return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            }
            return null;
        }
    }
}
