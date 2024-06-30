package com.freeing.batch.web.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freeing.batch.jdbc.convetor.StringConvertorRegistry;
import com.freeing.batch.jdbc.type.TypeHandlerRegistry;
import com.freeing.batch.config.JobConfig;
import com.freeing.batch.config.JobConfigBuilder;
import com.freeing.batch.web.modle.po.JobConfigItem;

public class JobConfigParser {
    private final JobConfigItem jobConfigItem;

    public JobConfigParser(JobConfigItem jobConfigItem) {
        this.jobConfigItem = jobConfigItem;
    }

    public JobConfig parse(TypeHandlerRegistry typeHandlerRegistry, StringConvertorRegistry stringConvertorRegistry) {
        JobConfigBuilder builder = new JobConfigBuilder(typeHandlerRegistry,
            stringConvertorRegistry,
            jobConfigItem.getJobName(),
            jobConfigItem.getJobName());

        builder.fieldTerminated(jobConfigItem.getFieldTerminated())
            .lineTerminated(jobConfigItem.getLineTerminated())
            .basePath(jobConfigItem.getBasePath())
            .chunk(jobConfigItem.getChunk())
            .suffix(jobConfigItem.getSuffix());

        parseColumnMappers(builder);

        return builder.build();
    }

    private void parseColumnMappers(JobConfigBuilder builder) {
        String json = jobConfigItem.getColumnsInfo();
        JSONArray array = JSONArray.parseArray(json);
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject objectMap = array.getJSONObject(i);
            String property = objectMap.getString("property");
            String jdbcType = objectMap.getString("jdbcType");
            String numericScale = objectMap.getString("numericScale");
            String pattern = objectMap.getString("pattern");

            builder.addConvertor(jdbcType, pattern);
            builder.addParameterMapping(property, jdbcType);
        }
    }
}
