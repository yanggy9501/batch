package com.freeing.batch.base.adpater;

import com.freeing.batch.config.JobConfig;
import com.freeing.batch.object.JobParams;

public interface JobConfigAdapter {
    JobConfig getJobConfig(JobParams jobParams);
}
