package com.freeing.batch.service;

import com.freeing.batch.object.JobParams;

import java.sql.SQLException;

public interface JobService {

    Object start(JobParams jobParams);
}
