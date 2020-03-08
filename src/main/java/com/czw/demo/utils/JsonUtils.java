package com.czw.demo.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson工具类
 *
 * @author caizw
 */
public class JsonUtils {

    private static Object lock = new Object();
    private static ObjectMapper mapper = null;

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            Object var0 = lock;
            synchronized(lock) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                }
            }
        }

        return mapper;
    }

}
