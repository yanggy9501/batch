package com.freeing.batch.web;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class WebBatchApplication {
    public static void main(String[] args) {
        System.setProperty("spring.batch.job.launcher.type", "restful");
        SpringApplication.run(WebBatchApplication.class, args);
    }
}
