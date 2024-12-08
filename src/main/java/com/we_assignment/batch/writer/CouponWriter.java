package com.we_assignment.batch.writer;


import com.we_assignment.entity.archived.ArchivedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CouponWriter {

    private final MongoTemplate mongoTemplate;

    @Bean
    public ItemWriter<ArchivedCoupon> writer() {
        return items -> {
            mongoTemplate.insertAll(new ArrayList<>(items.getItems()));
        };
    }
}

