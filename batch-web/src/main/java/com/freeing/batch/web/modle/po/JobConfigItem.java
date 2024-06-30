package com.freeing.batch.web.modle.po;

/*
CREATE TABLE `job_config_item` (
  `job_name` varchar(255) NOT NULL,
  `table_name` varchar(255) NOT NULL,
  `base_path` varchar(400) NOT NULL,
  `suffix` varchar(255) NOT NULL COMMENT '文件base后缀，多个后缀用逗号隔开',
  `field_terminated` varchar(8) NOT NULL,
  `columns_info` varchar(2000) NOT NULL COMMENT 'JSON',
  `chunk` int  NULL,
  PRIMARY KEY (`job_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 */
public class JobConfigItem {
    private String jobName;

    private String tableName;

    private String basePath;

    private String suffix;

    private String fieldTerminated;

    private String lineTerminated;

    // JSON
    private String columnsInfo;

    private int chunk = 2000;

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

    public String getColumnsInfo() {
        return columnsInfo;
    }

    public void setColumnsInfo(String columnsInfo) {
        this.columnsInfo = columnsInfo;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }
}
