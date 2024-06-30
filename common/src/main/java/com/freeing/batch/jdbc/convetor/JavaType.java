package com.freeing.batch.jdbc.convetor;

public enum JavaType {
    INT("int"),
    INTEGER("integer"),
    LONG("long"),
    LONG_("Long"),
    DATE("date"),
    DECIMAL("decimal"),
    BIG_DECIMAL("bigDecimal"),
    BOOLEAN("boolean"),
    BOOL("bool"),
    ;

    private String type;

    JavaType(String type) {
        this.type = type;
    }

    public JavaType match(String type) {
        for (JavaType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }
}
