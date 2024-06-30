package com.freeing.batch.jdbc.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum JdbcType {

    INTEGER(Types.INTEGER),
    INT(Types.INTEGER),
    BIGINT(Types.BIGINT),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    LONGVARCHAR(Types.LONGVARCHAR),
    TEXT(Types.LONGVARCHAR),
    DATE(Types.DATE),
    TIME(Types.TIME),
    TIMESTAMP(Types.TIMESTAMP),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    BOOLEAN(Types.BOOLEAN),
    ;

    public final int TYPE_CODE;
    private static Map<Integer, JdbcType> codeLookup = new HashMap<>();
    private static Map<String, JdbcType> nameLookup = new HashMap<>();

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
            nameLookup.put(type.name(), type);
        }
    }

    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    public static JdbcType forCode(int code) {
        return codeLookup.get(code);
    }

    public static JdbcType forType(String type) {
        if (type == null) {
            return NULL;
        }
        return nameLookup.get(type.toUpperCase(Locale.ENGLISH));
    }

}
