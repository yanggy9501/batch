package com.freeing.batch.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    /**
     * 设置 jdbc PreparedStatement 参数
     *
     * @param ps        PreparedStatement
     * @param index     参数 index
     * @param parameter 参数值
     * @param jdbcType  jdbc 类型
     * @throws SQLException
     */
    void setParameter(PreparedStatement ps, int index, T parameter, JdbcType jdbcType) throws SQLException;

    T getResult(ResultSet rs, String columnName) throws SQLException;

    T getResult(ResultSet rs, int columnIndex) throws SQLException;

    T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
