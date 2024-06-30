package com.freeing.batch.base.adpater;

import com.freeing.batch.config.JobConfig;
import com.freeing.batch.object.JobParams;
import org.springframework.stereotype.Service;

@Service
public class XmlJobConfigAdapter implements JobConfigAdapter {
    @Override
    public JobConfig getJobConfig(JobParams jobParams) {

        return null;
    }
}
