package com.yqz.console.tech.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonHelper {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.registerModule(new JavaTimeModule());
    }

    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object o,boolean prettyPrinter) {
        try { 
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<T> clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
           log.warn(e.getMessage());
        }
        return null;
    }
    public static <T> T deserialize(String s, TypeReference clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            log.error(s+", "+e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, JavaType clazz) {
        try {
            T t = mapper.readValue(s, clazz);
            return t;
        } catch (IOException e) {
            log.error(s+", "+e.getMessage());
        }
        return null;
    }

    public static <T> T deserialize(String s, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            T t = mapper.readValue(s, TypeFactory.defaultInstance().constructParametricType(parametrized,parameterClasses));
            return t;
        } catch (IOException e) {
            log.error(s+", "+e.getMessage());
        }
        return null;
    }
}
