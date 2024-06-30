package com.freeing.batch.web.controller;

import com.freeing.batch.object.JobParams;
import com.freeing.batch.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    @Qualifier("defaultJobServiceImpl")
    private JobService syncJobService;

    @RequestMapping("/start")
    public Object start(JobParams jobParams) {
        return syncJobService.start(jobParams);
    }
}
