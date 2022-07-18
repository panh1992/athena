package org.athena.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.jackson.Jackson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.athena.plugin.jackson.InstantDeserializer;
import org.athena.plugin.jackson.InstantSerializer;
import org.athena.plugin.jackson.NumberSerializer;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * 通用帮助类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtil {

    /**
     * 获取系统 jackson 配置的 ObjectMapper 信息
     */
    @Provides
    @Singleton
    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = Jackson.newObjectMapper();
        // 禁用空对象转换json校验
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ==========  序列化/反序列化 处理  ==============
        SimpleModule instantModule = new SimpleModule("instant");
        instantModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        instantModule.addDeserializer(Instant.class, InstantDeserializer.INSTANCE);
        mapper.registerModule(instantModule);

        SimpleModule numberModule = new SimpleModule("number");
        numberModule.addSerializer(Number.class, NumberSerializer.INSTANCE);
        mapper.registerModule(numberModule);

        return mapper;
    }

    /**
     * 将数据转换为 byte 数组
     *
     * @param data 待转换字符串
     * @return byte 数组
     */
    public static byte[] convertBytes(String data) {

        return data.getBytes(StandardCharsets.UTF_8);

    }

}
