package com.freeing.batch.jdbc.convetor.string;

import com.freeing.batch.jdbc.exception.ConvertorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class String2DateConvertor extends StringConvertor<Date> {

    public static final String defaultPattern = "yyyy-MM-dd HH:mm:ss";

    private final String format;

    public String2DateConvertor() {
        format = defaultPattern;
    }

    public String2DateConvertor(String format) {
        this.format = format;
    }

    @Override
    public Date convertor(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            throw new ConvertorException(e);
        }
    }
}
