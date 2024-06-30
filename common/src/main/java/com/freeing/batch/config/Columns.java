package com.freeing.batch.config;

import com.freeing.batch.enums.SplitType;

import java.util.ArrayList;
import java.util.List;

public class Columns {
    private SplitType splitType;

    private List<Column> columns = new ArrayList<>();

    public Columns add(Column column) {
        columns.add(column);
        return this;
    }

    public static class Column {
        private String alias;

        private String type;

        /** Column fix length */
        private Integer length;

        /** Date 格式 */
        private String format;

        /** bool 规则，如：真=true,yes,y,1 */
        private String trueRule;
        private String falseRule;

        public Column(String alias, String type, Integer length, String format, String trueRule, String falseRule) {
            this.alias = alias;
            this.type = type;
            this.length = length;
            this.format = format;
            this.trueRule = trueRule;
            this.falseRule = falseRule;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getTrueRule() {
            return trueRule;
        }

        public void setTrueRule(String trueRule) {
            this.trueRule = trueRule;
        }

        public String getFalseRule() {
            return falseRule;
        }

        public void setFalseRule(String falseRule) {
            this.falseRule = falseRule;
        }
    }

    public static class ColumnBuilder {
        private String alias;

        private String type;

        private Integer length;

        private String format;

        private String trueRule;
        private String falseRule;

        public ColumnBuilder(String alias, String type) {
            this.alias = alias;
            this.type = type;
        }

        public ColumnBuilder length(Integer length) {
            this.length = length;
            return this;
        }

        public ColumnBuilder format(String format) {
            this.format = format;
            return this;
        }

        public ColumnBuilder trueRule(String trueRule) {
            this.trueRule = trueRule;
            return this;
        }

        public ColumnBuilder falseRule(String falseRule) {
            this.falseRule = falseRule;
            return this;
        }

        public Column build() {
            return new Column(alias, type, length, format, trueRule, falseRule);
        }
    }
}
