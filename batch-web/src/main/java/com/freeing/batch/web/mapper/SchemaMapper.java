package com.freeing.batch.web.mapper;

import com.freeing.batch.web.modle.db.Table;
import com.freeing.batch.web.modle.db.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SchemaMapper {

    @Select("SELECT TABLE_SCHEMA AS tableSchema, TABLE_NAME AS tableName, TABLE_COMMENT AS tableComment " +
        "FROM `information_schema`.`TABLES` " +
        "WHERE TABLE_SCHEMA = #{tableSchema} AND `TABLE_NAME` = #{tableName}")
    Table getTable(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    @Select("SELECT TABLE_SCHEMA AS tableSchema, TABLE_NAME AS tableName, COLUMN_NAME AS columnName, " +
        "ORDINAL_POSITION AS ordinalPosition, COLUMN_COMMENT AS columnComment  " +
        "FROM `information_schema`.`COLUMNS` " +
        "WHERE TABLE_SCHEMA = #{tableSchema} AND `TABLE_NAME` = #{tableName}")
    Column[] queryColumns(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);
}
