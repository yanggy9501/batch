package com.freeing.batch.web.service.impl;

import com.freeing.batch.jdbc.convetor.Convertor;
import com.freeing.batch.jdbc.convetor.StringConvertorRegistry;
import com.freeing.batch.jdbc.mapping.ParameterMapping;
import com.freeing.batch.jdbc.type.TypeHandlerRegistry;
import com.freeing.batch.web.mapper.JobConfigItemMapper;
import com.freeing.batch.config.JobConfig;
import com.freeing.batch.web.modle.po.JobConfigItem;
import com.freeing.batch.object.JobParams;
import com.freeing.batch.web.parser.JobConfigParser;
import com.freeing.batch.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DefaultJobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobConfigItemMapper jobConfigItemMapper;

    @Override
    public Object start(JobParams jobParams) {
        JobConfigItem jobConfigItem = jobConfigItemMapper.getJobConfigItem(jobParams.getJobName());

        if (jobConfigItem == null) {

        }
        String bizDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        JobConfigParser jobConfigParser = new JobConfigParser(jobConfigItem);
        JobConfig jobConfig = jobConfigParser.parse(new TypeHandlerRegistry(), new StringConvertorRegistry());
        Job job = buildBatchJob(jobConfig, bizDate);

        // 启动job作业
        JobParameters jp = new JobParametersBuilder(jobExplorer)
            .addString("bizDate", bizDate)
            .addString("uuid", jobParams.getUuid())
            .toJobParameters();

        JobExecution jobExecution;
        try {
            jobExecution = jobLauncher.run(job, jp);
        } catch (JobExecutionAlreadyRunningException e) {
            return "Job execution already running";
        } catch (JobRestartException e) {
            return "job restart";
        } catch (JobInstanceAlreadyCompleteException e) {
            return "Job instance already complete";
        } catch (JobParametersInvalidException e) {
            return "Job parameters invalid";
        }
        return jobExecution.getExitStatus();
    }

    private Job buildBatchJob(JobConfig jobConfig, String bizDate) {
        return jobBuilderFactory.get(jobConfig.getJobName())
            .start(prePrepareStep(jobConfig))
            .next(fileRead2Process2WriteStep(jobConfig, bizDate))
            .build();
    }

    private Step prePrepareStep(JobConfig jobConfig) {
        return stepBuilderFactory.get("pre_prepare_step_" + jobConfig.getJobName())
            .tasklet(new Tasklet() {
                // 预准备业务逻辑
                @Override
                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                    logger.info("checking......");
                    // 成功 ...

                    // 失败(抛出异常或者设置失败状态) ...

                    // 该步骤成功或失败完成，使用该方式结束完成需要使用  contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }
            })
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    // 步骤执行前
                    logger.info("Start checking job pre condition.");
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    // 检查 - 成功
                    if (stepExecution.getExitStatus() == ExitStatus.COMPLETED) {
                        logger.info("pass preChecking.");
                    } else {
                        logger.warn("Pre checking failure.");
                    }
                    return stepExecution.getExitStatus();
                }
            })
            .build();

    }

    private Step fileRead2Process2WriteStep(JobConfig jobConfig, String bizDate) {
        return stepBuilderFactory.get("step_sync_data_" + jobConfig.getJobName())
            .<Object[], Object[]>chunk(jobConfig.getChunk())
            .reader(flatFileItemReader(jobConfig, bizDate))
            .processor(objectItemProcessor(jobConfig))
            .writer(objectItemWriter(jobConfig))
            .listener(new ItemWriteListener<Object[]>() {
                @Override
                public void beforeWrite(List<? extends Object[]> items) {
                    logger.info("writing {} row data.", items.size());
                }

                @Override
                public void afterWrite(List<? extends Object[]> items) {

                }

                @Override
                public void onWriteError(Exception exception, List<? extends Object[]> items) {

                }
            })
            .build();
    }

    private FlatFileItemReader<Object[]> flatFileItemReader(JobConfig jobConfig, String bizDate) {
        return new FlatFileItemReaderBuilder<Object[]>()
            .name("reader_" + jobConfig.getJobName())
            .resource(new FileSystemResource(jobConfig.getResource(bizDate)))
            .delimited().delimiter(jobConfig.getFieldTerminated())
            .names(jobConfig.getColumnNames())
            .fieldSetMapper(new FieldSetMapper<Object[]>() {
                @Override
                public Object[] mapFieldSet(FieldSet fieldSet) {
                    List<ParameterMapping> parameterMappings = jobConfig.getParameterMappings();
                    Object[] values = new Object[parameterMappings.size()];
                    for (int i = 0, len = values.length; i < len; i++) {
                        values[i] = fieldSet.readString(i);
                    }
                    return values;
                }
            })
            .build();
    }

    private ItemProcessor<Object[], Object[]> objectItemProcessor(JobConfig jobConfig) {
        List<Convertor> parameterConvertors = jobConfig.getParameterConvertors();
        return new ItemProcessor<Object[], Object[]>() {
            @Override
            public Object[] process(Object[] values) throws Exception {
                int idx = 0;
                for (Convertor parameterMapping : parameterConvertors) {
                    values[idx] = parameterMapping.convertor(values[idx]);
                    idx++;
                }
                return values;
            }
        };
    }

    private ItemWriter<Object[]> objectItemWriter(JobConfig jobConfig) {
        List<ParameterMapping> parameterMappings = jobConfig.getParameterMappings();
        return new JdbcBatchItemWriterBuilder<Object[]>()
            .dataSource(dataSource)
            .sql(jobConfig.getInsertTmpTableSql())
            .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Object[]>() {
                @Override
                public void setValues(Object[] values, PreparedStatement preparedStatement) throws SQLException {
                    for (int i = 0, size = parameterMappings.size(); i < size; i++) {
                        ParameterMapping parameterMapping = parameterMappings.get(i);
                        parameterMapping
                            .getTypeHandler()
                            .setParameter(preparedStatement, i + 1, values[i], parameterMapping.getJdbcType());
                    }
                }
            })
            .build();
    }

}
