package com.freeing.batch.object;

/**
 * 作业参数
 */
public class JobParams {
    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 作业业务日期（非执行日期）
     */
    private String bizDate;

    private String uuid;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }
}
