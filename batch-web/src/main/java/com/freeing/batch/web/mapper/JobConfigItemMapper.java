package com.freeing.batch.web.mapper;

import com.freeing.batch.web.modle.po.JobConfigItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JobConfigItemMapper {

    @Select("SELECT " +
        "JOB_NAME AS jobName," +
        "table_name AS tableName," +
        "base_path AS basePath," +
        "suffix," +
        "field_terminated AS fieldTerminated," +
        "columns_info AS columnsInfo," +
        "chunk " +
        "FROM " +
        "job_config_item " +
        "WHERE " +
        "job_name = #{jobName};")
    JobConfigItem getJobConfigItem(String jobName);
}
