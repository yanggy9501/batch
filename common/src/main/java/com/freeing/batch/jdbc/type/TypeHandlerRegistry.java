package com.freeing.batch.jdbc.type;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TypeHandlerRegistry {

    /**
     * 数据库数据类型对应的 TypeHandler
     */
    private final Map<JdbcType, TypeHandler<?>> jdbcTypeHandlerMap = new EnumMap<>(JdbcType.class);

    /**
     * Java 类型对应的 TypeHandler key（Type）：Java 的类型 value（Map<JdbcType, TypeHandler<?>>）：jdbcTypeHandlerMap 即 数据库类型中就会很多中如
     * char varchar text 就能和 java String 匹配。 如果拿不到 JdbcType（没写）也是可以了，根据目标的 java 类型就可以了（获取 map 的第一个）
     * 即：有精确的使用精确的，没有则取一个
     */
    private final Map<Type, Map<JdbcType, TypeHandler<?>>> typeHandlerMap = new ConcurrentHashMap<>();

    /**
     * 兜底方案：转成 Object
     */
    private final TypeHandler<Object> unknownTypeHandler = new UnknownTypeHandler(this);

    /**
     * 所有的类型转换器（缓存所有）
     */
    private final Map<Class<?>, TypeHandler<?>> allTypeHandlersMap = new HashMap<>();

    private static final Map<JdbcType, TypeHandler<?>> NULL_TYPE_HANDLER_MAP = Collections.emptyMap();

    public TypeHandlerRegistry() {
        register(Boolean.class, new BooleanTypeHandler());
        register(boolean.class, new BooleanTypeHandler());
        register(JdbcType.BOOLEAN, new BooleanTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());
        register(JdbcType.INTEGER, new IntegerTypeHandler());
        register(JdbcType.INT, new IntegerTypeHandler());

        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());


        register(Double.class, new DoubleTypeHandler());
        register(double.class, new DoubleTypeHandler());
        register(JdbcType.DOUBLE, new DoubleTypeHandler());

        register(String.class, new StringTypeHandler());
        register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
        register(String.class, JdbcType.LONGVARCHAR, new StringTypeHandler());

        register(JdbcType.VARCHAR, new StringTypeHandler());
        register(JdbcType.LONGVARCHAR, new StringTypeHandler());

        register(BigInteger.class, new BigIntegerTypeHandler());
        register(JdbcType.BIGINT, new LongTypeHandler());

        register(BigDecimal.class, new BigDecimalTypeHandler());
        register(JdbcType.DECIMAL, new BigDecimalTypeHandler());

        register(Object.class, unknownTypeHandler);
        register(Object.class, JdbcType.OTHER, unknownTypeHandler);
        register(JdbcType.OTHER, unknownTypeHandler);

        register(Date.class, new DateTypeHandler());
        register(Date.class, JdbcType.DATE, new DateOnlyTypeHandler());
        register(Date.class, JdbcType.TIME, new TimeOnlyTypeHandler());
        register(JdbcType.TIMESTAMP, new DateTypeHandler());
        register(JdbcType.DATE, new DateOnlyTypeHandler());
        register(JdbcType.TIME, new TimeOnlyTypeHandler());


        register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        register(LocalDate.class, new LocalDateTypeHandler());
        register(LocalTime.class, new LocalTimeTypeHandler());

        register(Character.class, new CharacterTypeHandler());
        register(char.class, new CharacterTypeHandler());
    }

    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }

    public boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return javaType != null && getTypeHandler((Type) javaType, jdbcType) != null;
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler((Type) type, null);
    }


    public TypeHandler<?> getTypeHandler(JdbcType jdbcType) {
        return jdbcTypeHandlerMap.get(jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType) {
        return getTypeHandler((Type) type, jdbcType);
    }

    @SuppressWarnings("unchecked")
    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType) {
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = getJdbcHandlerMap(type);
        TypeHandler<?> handler = null;
        if (jdbcHandlerMap != null) {
            handler = jdbcHandlerMap.get(jdbcType);
            if (handler == null) {
                handler = jdbcHandlerMap.get(null);
            }
            if (handler == null) {
                handler = pickSoleHandler(jdbcHandlerMap);
            }
        }
        return (TypeHandler<T>) handler;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMap(Type type) {
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = typeHandlerMap.get(type);
        if (NULL_TYPE_HANDLER_MAP.equals(jdbcHandlerMap)) {
            return null;
        }
        if (jdbcHandlerMap == null && type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            jdbcHandlerMap = getJdbcHandlerMapForSuperclass(clazz);
        }
        typeHandlerMap.put(type, jdbcHandlerMap == null ? NULL_TYPE_HANDLER_MAP : jdbcHandlerMap);
        return jdbcHandlerMap;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMapForSuperclass(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || Object.class.equals(superclass)) {
            return null;
        }
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = typeHandlerMap.get(superclass);
        if (jdbcHandlerMap != null) {
            return jdbcHandlerMap;
        } else {
            return getJdbcHandlerMapForSuperclass(superclass);
        }
    }

    private TypeHandler<?> pickSoleHandler(Map<JdbcType, TypeHandler<?>> jdbcHandlerMap) {
        TypeHandler<?> soleHandler = null;
        for (TypeHandler<?> handler : jdbcHandlerMap.values()) {
            if (soleHandler == null) {
                soleHandler = handler;
            } else if (!handler.getClass().equals(soleHandler.getClass())) {
                // More than one type handlers registered.
                return null;
            }
        }
        return soleHandler;
    }

    public TypeHandler<Object> getUnknownTypeHandler() {
        return unknownTypeHandler;
    }

    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        jdbcTypeHandlerMap.put(jdbcType, handler);
    }


    @SuppressWarnings("unchecked")
    public <T> void register(TypeHandler<T> typeHandler) {
        register((Class<T>) null, typeHandler);
    }

    // java type + handler

    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        register((Type) javaType, typeHandler);
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        register(javaType, null, typeHandler);
    }


    public <T> void register(Class<T> type, JdbcType jdbcType, TypeHandler<? extends T> handler) {
        register((Type) type, jdbcType, handler);
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (javaType != null) {
            Map<JdbcType, TypeHandler<?>> map = typeHandlerMap.get(javaType);
            if (map == null || map == NULL_TYPE_HANDLER_MAP) {
                map = new HashMap<>();
                typeHandlerMap.put(javaType, map);
            }
            // 同一个 JavaType 可以对应多个 JdbcType
            map.put(jdbcType, handler);
        }
        allTypeHandlersMap.put(handler.getClass(), handler);
    }

}
