package com.freeing.batch.jdbc.convetor;

import com.freeing.batch.jdbc.convetor.string.StringConvertor;

public class DefaultConvertor extends StringConvertor<String> implements Convertor<String, String> {
    @Override
    public String convertor(String s) {
        return s;
    }
}
