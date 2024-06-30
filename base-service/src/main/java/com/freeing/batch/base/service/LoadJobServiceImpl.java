package com.freeing.batch.base.service;

import com.freeing.batch.base.adpater.JobConfigAdapter;
import com.freeing.batch.config.JobConfig;
import com.freeing.batch.object.JobParams;
import com.freeing.batch.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 show global variables like 'local_infile';
 set global local_infile='ON';
 ----> allowLoadLocalInfile=true
 url: jdbc:mysql://127.0.0.1:3306/db_test?serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true
 */
@Service
public class LoadJobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobConfigAdapter jobConfigAdapter;

    public Object start(JobParams jobParams) {
        JobConfig jobConfig = jobConfigAdapter.getJobConfig(jobParams);
        if (jobParams.getBizDate() == null || jobParams.getBizDate().trim().isEmpty()) {
            jobParams.setBizDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        } else {
            jobParams.setBizDate(jobParams.getBizDate().trim());
        }
        String bizDate = jobParams.getBizDate();

        String res = "ok";
        /* --------------------------- Load  --------------------------*/
        String loadSql = jobConfig.getLoadSql(bizDate);

        Connection connection = null;
        Statement statement = null;
        try {
            logger.info("Load 数据：sql={}", loadSql);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            int row = statement.executeUpdate(loadSql);
            logger.info("Load 行数 {}", row);
            connection.commit();
        } catch (SQLException e) {
            logger.info("Load error.", e);
            res = e.getMessage();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.info("Close error.", e);
                    res = e.getMessage();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.info("Close error.", e);
                    res = e.getMessage();
                }
            }
        }
        return res;
    }
}
