package com.we_assignment.batch.reader;

import com.we_assignment.entity.Coupon;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CouponReader {
    @Bean
    public JpaPagingItemReader<Coupon> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Coupon>()
                .name("couponReader")
                .queryString("SELECT c FROM Coupon c WHERE c.expiredAt < :now")
                .parameterValues(Map.of("now", LocalDateTime.now()))
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .build();
    }
}