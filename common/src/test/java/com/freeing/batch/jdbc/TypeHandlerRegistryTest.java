package com.freeing.batch.jdbc;

import com.freeing.batch.jdbc.type.JdbcType;
import com.freeing.batch.jdbc.type.TypeHandler;
import com.freeing.batch.jdbc.type.TypeHandlerRegistry;


public class TypeHandlerRegistryTest {
    public static void main(String[] args) {
        TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
        TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(JdbcType.VARCHAR);

        System.out.println(typeHandler);
    }
}
