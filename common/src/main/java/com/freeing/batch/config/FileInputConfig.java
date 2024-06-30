package com.freeing.batch.config;

import com.freeing.batch.enums.SplitType;
import com.freeing.batch.jdbc.convetor.Convertor;
import com.freeing.batch.jdbc.mapping.ParameterMapping;

import java.util.List;

public class FileInputConfig {
    private final String type = "file";

    private

    private SplitType splitType;

    /**
     * 目标表名
     */
    private String tableName;

    /**
     * 待解析文件所在 base 路径
     */
    private String basePath;

    /**
     * 待解析文件后缀名
     */
    private String suffix;

    /**
     * 字段间分隔符
     */
    private String fieldTerminated;

    /**
     * 行间数据分隔符
     */
    private String lineTerminated;

    /**
     * jdbc 与 java 映射
     */
    private List<ParameterMapping> parameterMappings;

    /**
     * 字段的数据转换器
     */
    List<Convertor> parameterConvertors;
}
