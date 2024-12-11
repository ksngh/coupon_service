package com.we_assignment.service.coupon;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponBatchService {
    private final JobLauncher jobLauncher;
    private final Job archiveCouponsJob;

    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
    public void runCouponArchiveJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("sixMonthsAgo", LocalDateTime.now().minusMonths(6).toString())
                .toJobParameters();

        jobLauncher.run(archiveCouponsJob, jobParameters);
    }
}