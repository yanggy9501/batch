package com.freeing.batch.jdbc.convetor;


public interface Convertor<IN, OUT> {

    OUT convertor(IN in);
}
