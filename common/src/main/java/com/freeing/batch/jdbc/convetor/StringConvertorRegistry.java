package com.freeing.batch.jdbc.convetor;

import com.freeing.batch.jdbc.convetor.string.StringConvertor;

import java.util.HashMap;
import java.util.Map;

public class StringConvertorRegistry {

    private final Map<JavaType, Map<String, StringConvertor<?>>> convertorMap = new HashMap<>();

    public StringConvertorRegistry() {

    }

    public void registry(JavaType javaType, String ns, StringConvertor<?> convertor) {
        Map<String, StringConvertor<?>> map = convertorMap.computeIfAbsent(javaType, k -> new HashMap<>());
        if (map.containsKey(ns)) {
            return;
        }
        map.put(ns, convertor);
    }


}
