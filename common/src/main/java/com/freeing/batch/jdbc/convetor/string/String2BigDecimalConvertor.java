package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

import java.math.BigDecimal;

public class String2BigDecimalConvertor extends StringConvertor<BigDecimal> {

    @Override
    public BigDecimal convertor(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            throw new ConvertorException(e);
        }
    }
}
