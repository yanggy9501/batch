package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

public class String2IntegerConvertor extends StringConvertor<Integer> {

    @Override
    public Integer convertor(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            throw new ConvertorException(e);
        }
    }
}
