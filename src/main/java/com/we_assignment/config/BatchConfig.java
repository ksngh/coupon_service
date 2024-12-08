package com.we_assignment.config;

import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.archived.ArchivedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Job archiveCouponsJob(Step archiveCouponsStep) {
        return new JobBuilder("archiveCouponsJob", jobRepository)
                .start(archiveCouponsStep)
                .build();
    }

    @Bean
    public Step archiveCouponsStep(JpaPagingItemReader<Coupon> couponReader,
                                   ItemProcessor<Coupon, ArchivedCoupon> couponProcessor,
                                   ItemWriter<ArchivedCoupon> couponWriter) {
        return new StepBuilder("archiveCouponsStep", jobRepository)
                .<Coupon, ArchivedCoupon>chunk(100, transactionManager)
                .reader(couponReader)
                .processor(couponProcessor)
                .writer(couponWriter)
                .build();
    }
}
