package com.we_assignment.batch.processor;

import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.archived.ArchivedCoupon;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CouponProcessor {
    @Bean
    public ItemProcessor<Coupon, ArchivedCoupon> processor() {
        return coupon -> ArchivedCoupon.of(
                coupon.getId(),
                coupon.getCode(),
                coupon.isRedeemed(),
                coupon.isActive(),
                coupon.getExpiredAt(),
                coupon.getCouponTopic().getId(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt(),
                coupon.getDeletedAt()
        );
    }
}
