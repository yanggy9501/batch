package com.freeing.batch.config;

import com.freeing.batch.jdbc.convetor.Convertor;
import com.freeing.batch.jdbc.convetor.StringConvertorRegistry;
import com.freeing.batch.jdbc.mapping.ParameterMapping;
import com.freeing.batch.jdbc.type.JdbcType;
import com.freeing.batch.jdbc.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class JobConfigBuilder {
    private TypeHandlerRegistry typeHandlerRegistry;
    private StringConvertorRegistry stringConvertorRegistry;

    private final String jobName;

    private final String tableName;

    private String basePath;

    private String suffix;

    private String fieldTerminated;

    private String lineTerminated;

    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    List<Convertor> parameterConvertors = new ArrayList<>();

    private int chunk;

    public JobConfig build() {
        JobConfig jobConfig = new JobConfig();

        jobConfig.setJobName(jobName);
        jobConfig.setTableName(tableName);
        jobConfig.setFieldTerminated(fieldTerminated);
        jobConfig.setLineTerminated(lineTerminated);
        jobConfig.setBasePath(basePath);
        jobConfig.setSuffix(suffix);
        jobConfig.setParameterConvertors(parameterConvertors);
        jobConfig.setParameterMappings(parameterMappings);
        jobConfig.setChunk(this.chunk);
        return jobConfig;
    }

    public JobConfigBuilder(TypeHandlerRegistry typeHandlerRegistry,
        StringConvertorRegistry stringConvertorRegistry,
        String jobName,
        String tableName) {
        this.typeHandlerRegistry = typeHandlerRegistry;
        this.stringConvertorRegistry = stringConvertorRegistry;
        this.jobName = jobName;
        this.tableName = tableName;
    }

    public JobConfigBuilder fieldTerminated(String fieldTerminated) {
        this.fieldTerminated = fieldTerminated;
        return this;
    }

    public JobConfigBuilder lineTerminated(String lineTerminated) {
        this.lineTerminated = lineTerminated;
        return this;
    }

    public JobConfigBuilder parameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings.addAll(new ArrayList<>(parameterMappings));
        return this;
    }

    public JobConfigBuilder addParameterMapping(String property, String jdbcType) {
        ParameterMapping parameterMapping =
            new ParameterMapping.Builder(property, typeHandlerRegistry.getTypeHandler(JdbcType.forType(jdbcType)))
            .jdbcTypeName(jdbcType)
            .jdbcTypeName(jdbcType)
            .build();
        parameterMappings.add(parameterMapping);

        return this;
    }

    public JobConfigBuilder addConvertor(String jdbcType) {
        parameterConvertors.add(stringConvertorRegistry.getConvertor(JdbcType.forType(jdbcType)));
        return this;
    }

    public JobConfigBuilder addConvertor(String jdbcType, String pattern) {
        parameterConvertors.add(stringConvertorRegistry.getConvertor(JdbcType.forType(jdbcType), pattern));
        return this;
    }

    public JobConfigBuilder basePath(String bashPath) {
        this.basePath = bashPath;
        return this;
    }

    public JobConfigBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public JobConfigBuilder chunk(int chunk) {
        this.chunk = chunk;
        return this;
    }
}
