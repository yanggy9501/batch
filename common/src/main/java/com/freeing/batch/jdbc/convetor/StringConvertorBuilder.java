package com.freeing.batch.jdbc.convetor;

import com.freeing.batch.jdbc.convetor.string.*;

import java.util.Map;

public class StringConvertorBuilder {

    public static StringConvertor<?> instance(JavaType javaType, Map<String, Object> convertorRules) {
        switch (javaType){
            case INT: return new String2IntConvertor((Integer) convertorRules.get("emptyTo"));
            case INTEGER: return new String2IntegerConvertor();
            case DATE: return new String2DateConvertor((String) convertorRules.get("format"));
            case LONG_: return new String2LongObjectConvertor();
            case LONG: return new String2LongConvertor((Long) convertorRules.get("emptyTo"));
            case DECIMAL: return new String2BigDecimalConvertor();
            case BIG_DECIMAL: return new String2BigDecimalConvertor();
            case BOOLEAN: return new String2BooleanConvertor((String) convertorRules.get("trueRule"), (String) convertorRules.get("falseRule"));
        }
        return new DefaultConvertor();
    }
}
