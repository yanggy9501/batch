package com.freeing.batch.jdbc.mapping;

import com.freeing.batch.jdbc.type.JdbcType;
import com.freeing.batch.jdbc.type.TypeHandler;

public class ParameterMapping {
    /**
     * 数据库 column 名
     */
    private String column;

    private JdbcType jdbcType;

    /**
     * 精度
     */
    private Integer numericScale;


    private TypeHandler<?> typeHandler;

    private ParameterMapping() {
    }

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(String column, TypeHandler<?> typeHandler) {
            parameterMapping.column = column;
            parameterMapping.typeHandler = typeHandler;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public Builder numericScale(Integer numericScale) {
            parameterMapping.numericScale = numericScale;
            return this;
        }

        public Builder typeHandler(TypeHandler<?> typeHandler) {
            parameterMapping.typeHandler = typeHandler;
            return this;
        }


        public ParameterMapping build() {
            validate();
            return parameterMapping;
        }

        private void validate() {
            if (parameterMapping.typeHandler == null) {
                throw new IllegalStateException("Type handler was null on parameter mapping for column '"
                    + parameterMapping.column + "'. It was either not specified and/or could not be found for the javaType ("
                    + ") : jdbcType (" + parameterMapping.jdbcType + ") combination.");
            }
        }

    }

    public String getColumn() {
        return column;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public Integer getNumericScale() {
        return numericScale;
    }

    /**
     * 注意：返回值不要带 ？ 泛型
     * @return
     */
    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParameterMapping{");
        sb.append("column='").append(column).append('\'');
        sb.append(", jdbcType=").append(jdbcType);
        sb.append(", numericScale=").append(numericScale);
        sb.append('}');
        return sb.toString();
    }
}
