package com.freeing.batch.config;

import com.freeing.batch.jdbc.convetor.Convertor;
import com.freeing.batch.jdbc.mapping.ParameterMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JobConfig {
    /**
     * 任务名称
     */
    private String jobName;

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

    /**
     * 批量大小
     */
    private int chunk = 1000;

    public String getTmpTableName() {
        return tableName + "_tmp";
    }

    public String getBackupTableName(LocalDateTime startDateTime) {
        return tableName + "_bak_" + startDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String[] getColumnNames() {
        return this.parameterMappings.stream()
            .map(ParameterMapping::getColumn)
            .toArray(String[]::new);
    }

    public String getInsertTmpTableSql() {
        int size = this.getColumnNames().length;
        String[] placeholders = new String[size];
        for (int i = 0; i < size; i++) {
            placeholders[i] = "?";
        }
        String insertSql = "INSERT INTO " + this.getTmpTableName() + " (" + String.join(",", this.getColumnNames()) + ")"
            + "VALUES(" + String.join(",", placeholders) + ")";

        return insertSql;
    }

    public String getLoadSql(String bizDate) {
        return String.format(
            "LOAD DATA LOCAL INFILE '%s' INTO TABLE %s FIELDS TERMINATED BY '%s' LINES TERMINATED BY '%s';",
             getResource(bizDate), getTableName(), getFieldTerminated(), getLineTerminated());
    }

    public String getResource(String bizDate) {
        String path = this.basePath + "/" +bizDate + "/" + tableName + "_" + bizDate + "." + suffix;
        return standardPath(path);
    }

    private static String standardPath(String aPath) {
        String path = aPath == null ? "" : aPath.trim();
        if (path.isEmpty()) {
            return "";
        }

        if (path.equals("/")) {
            return path;
        }

        // 反斜杠"\\"转正"/"
        path = path.replaceAll("\\\\", "/");
        // 双正斜杠"//"去重 "/"
        while (path.contains("//")) {
            path = path.replaceAll("//", "/");
        }

        // 去掉结尾 "/"
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFieldTerminated() {
        return fieldTerminated;
    }

    public void setFieldTerminated(String fieldTerminated) {
        this.fieldTerminated = fieldTerminated;
    }

    public String getLineTerminated() {
        return lineTerminated;
    }

    public void setLineTerminated(String lineTerminated) {
        this.lineTerminated = lineTerminated;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

    public List<Convertor> getParameterConvertors() {
        return parameterConvertors;
    }

    public void setParameterConvertors(List<Convertor> parameterConvertors) {
        this.parameterConvertors = parameterConvertors;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }
}
