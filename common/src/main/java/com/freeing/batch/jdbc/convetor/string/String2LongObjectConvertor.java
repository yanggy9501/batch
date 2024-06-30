package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

public class String2LongObjectConvertor extends StringConvertor<Long> {

    @Override
    public Long convertor(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new ConvertorException(e);
        }
    }
}
