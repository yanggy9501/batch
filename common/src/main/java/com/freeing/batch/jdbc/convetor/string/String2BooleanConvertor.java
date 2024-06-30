package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

import java.util.HashMap;
import java.util.Map;

public class String2BooleanConvertor extends StringConvertor<Boolean> {

    private String trueRule = "true,yes,y,1";
    private String falseRule = "false,no,n,0";

    private final Map<String, Boolean> booleanMap = new HashMap<>();

    public String2BooleanConvertor() {

    }

    public String2BooleanConvertor(String trueRule, String falseRule) {
        this.trueRule = trueRule;
        this.falseRule = falseRule;
    }

    private void parseRule() {
        if (trueRule != null) {
            for (String s : trueRule.split(",")) {
                booleanMap.put(s.trim(), true);
            }
        }
        if (falseRule != null) {
            for (String s : falseRule.split(",")) {
                booleanMap.put(s.trim(), false);
            }
        }
    }

    @Override
    public Boolean convertor(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return booleanMap.get(s);
    }
}
