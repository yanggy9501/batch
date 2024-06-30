package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

public class String2LongConvertor extends StringConvertor<Long> {

    private Long emptyTo;

    public String2LongConvertor(Long emptyTo) {
        this.emptyTo = emptyTo;
    }

    @Override
    public Long convertor(String s) {
        if (s == null || s.isEmpty()) {
            return emptyTo;
        }
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new ConvertorException(e);
        }
    }
}
