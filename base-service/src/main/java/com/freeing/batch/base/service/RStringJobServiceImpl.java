package com.freeing.batch.base.service;

import com.freeing.batch.base.adpater.JobConfigAdapter;
import com.freeing.batch.config.JobConfig;
import com.freeing.batch.jdbc.convetor.Convertor;
import com.freeing.batch.jdbc.mapping.ParameterMapping;
import com.freeing.batch.object.JobParams;
import com.freeing.batch.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class RStringJobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobConfigAdapter jobConfigAdapter;

    @Override
    public Object start(JobParams jobParams) {
        if (jobParams.getBizDate() == null || jobParams.getBizDate().trim().isEmpty()) {
            jobParams.setBizDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        } else {
            jobParams.setBizDate(jobParams.getBizDate().trim());
        }
        String bizDate = jobParams.getBizDate();
        JobConfig jobConfig = jobConfigAdapter.getJobConfig(jobParams);
        Job job = buildBatchJob(jobConfig, bizDate);


        JobParametersBuilder parametersBuilder = new JobParametersBuilder(jobExplorer)
            .addString("bizDate", jobParams.getBizDate());
        // 指定uuid用于重试
        if (jobParams.getUuid() != null || !jobParams.getUuid().trim().isEmpty()) {
            parametersBuilder.addString("uuid", jobParams.getUuid());
        }
        JobParameters jp = parametersBuilder.toJobParameters();

        JobExecution jobExecution;
        try {
            // 启动job作业
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

    public Job buildBatchJob(JobConfig jobConfig, String bizDate) {
        return jobBuilderFactory.get(jobConfig.getJobName())
            .start(prePrepareStep(jobConfig))
            .next(fileRead2Process2WriteStep(jobConfig, bizDate))
            .incrementer(new RunIdIncrementer())
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
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    // 检查 - 成功
                    if (stepExecution.getExitStatus() == ExitStatus.COMPLETED) {

                    } else {

                    }
                    return stepExecution.getExitStatus();
                }
            })
            .build();
    }

    public Step fileRead2Process2WriteStep(JobConfig jobConfig, String bizDate) {
        return stepBuilderFactory.get("step_sync_data_" + jobConfig.getJobName())
            .<String[], String[]>chunk(jobConfig.getChunk())
            .reader(flatFileItemReader(jobConfig, bizDate))
            .processor(stringItemProcessor(jobConfig))
            .writer(stringItemWriter(jobConfig))
            .build();
    }

    private FlatFileItemReader<String[]> flatFileItemReader(JobConfig jobConfig, String bizDate) {
        return new FlatFileItemReaderBuilder<String[]>()
            .name("reader_" + jobConfig.getJobName())
            .resource(new FileSystemResource(jobConfig.getResource(bizDate)))
            .delimited().delimiter(jobConfig.getFieldTerminated())
            .names(jobConfig.getColumnNames())
            .fieldSetMapper(new FieldSetMapper<String[]>() {
                @Override
                public String[] mapFieldSet(FieldSet fieldSet) {
                    List<ParameterMapping> parameterMappings = jobConfig.getParameterMappings();
                    String[] values = new String[parameterMappings.size()];
                    for (int i = 0, len = values.length; i < len; i++) {
                        values[i] = fieldSet.readString(i);
                    }
                    return values;
                }
            })
            .build();
    }

    private ItemProcessor<String[], String[]> stringItemProcessor(JobConfig jobConfig) {
        List<Convertor> parameterConvertors = jobConfig.getParameterConvertors();
        return new ItemProcessor<String[], String[]>() {
            @Override
            public String[] process(String[] values) throws Exception {
                return values;
            }
        };
    }

    public ItemWriter<String[]> stringItemWriter(JobConfig jobConfig) {
        List<ParameterMapping> parameterMappings = jobConfig.getParameterMappings();
        return new JdbcBatchItemWriterBuilder<String[]>()
            .dataSource(dataSource)
            .sql(jobConfig.getInsertTmpTableSql())
            .itemPreparedStatementSetter(new ItemPreparedStatementSetter<String[]>() {
                @Override
                public void setValues(String[] values, PreparedStatement preparedStatement) throws SQLException {
                    for (int i = 0, size = parameterMappings.size(); i < size; i++) {
                        if (values[i] == null) {
                            preparedStatement.setObject(i + 1, null);
                        } else {
                            preparedStatement.setString(i + 1, values[i]);
                        }
                    }
                }
            })
            .build();
    }

}
