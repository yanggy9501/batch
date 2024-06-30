package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

public class String2IntConvertor extends StringConvertor<Integer> {

    private Integer emptyTo = 0;

    public String2IntConvertor(Integer emptyTo) {
        this.emptyTo = emptyTo;
    }

    @Override
    public Integer convertor(String s) {
        if (s == null || s.isEmpty()) {
            return emptyTo;
        }
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            throw new ConvertorException(e);
        }
    }
}
