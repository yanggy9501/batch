package com.freeing.batch.enums;

public enum SplitType {
    SEPARATOR("separator"),
    FIXED_LENGTH("fixedLength"),
    ;

    private String splitType;

    SplitType(String splitType) {
        this.splitType = splitType;
    }
}
