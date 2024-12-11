package com.we_assignment.batch.writer;


import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.archived.ArchivedCoupon;
import com.we_assignment.repository.jpa.CouponRedemptionRepository;
import com.we_assignment.repository.jpa.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponWriter {

    private final MongoTemplate mongoTemplate;
    private final CouponRepository couponRepository;
    private final CouponRedemptionRepository couponRedemptionRepository;

    @Bean
    public ItemWriter<ArchivedCoupon> couponItemwriter() {
        return items -> {
            mongoTemplate.insertAll(new ArrayList<>(items.getItems()));
        };
    }

    @Bean
    public ItemWriter<Coupon> deleteCouponItemWriter() {
        return chunk -> {

            chunk.getItems().forEach(coupon -> {
                couponRedemptionRepository.deleteByCouponId(coupon.getId());
            });

            couponRepository.deleteAll(chunk.getItems());
        };
    }

    @Bean
    public ItemWriter<ArchivedCoupon> compositeItemWriter(ItemWriter<ArchivedCoupon> couponItemwriter,
                                                          ItemWriter<Coupon> deleteCouponItemWriter) {
        return items -> {

            couponItemwriter.write(items);

            List<Coupon> couponsToDelete = items.getItems().stream()
                    .map(archivedCoupon -> Coupon.builder()
                            .id(archivedCoupon.getId())
                            .build())
                    .toList();

            Chunk<Coupon> couponChunk = new Chunk<>(couponsToDelete);
            deleteCouponItemWriter.write(couponChunk);
        };
    }
}

