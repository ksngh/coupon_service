package com.we_assignment.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.QCoupon;
import com.we_assignment.entity.QCouponTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomCouponRepositoryImpl implements CustomCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CouponResponseDto> findAllCouponDetails(BooleanExpression predicate, Pageable pageable) {

        QCoupon qCoupon = QCoupon.coupon;
        QCouponTopic qCouponTopic = QCouponTopic.couponTopic;

        List<CouponResponseDto> results = queryFactory
                .select(Projections.constructor(CouponResponseDto.class,
                        qCoupon.code,
                        qCoupon.expiredAt,
                        qCoupon.isRedeemed,
                        qCoupon.couponTopic.name,
                        qCoupon.couponTopic.description
                ))
                .from(qCoupon)
                .join(qCoupon.couponTopic, qCouponTopic)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .where(predicate)
                .fetchOne();

        // 4. Page 객체 반환
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public List<Coupon> findAllCouponsByCouponTopicId(UUID couponTopicId) {
        QCoupon qCoupon = QCoupon.coupon;
        QCouponTopic qCouponTopic = QCouponTopic.couponTopic;

        return queryFactory
                .select(qCoupon)
                .from(qCoupon)
                .join(qCoupon.couponTopic, qCouponTopic).fetchJoin()
                .where(qCouponTopic.id.eq(couponTopicId))
                .fetch();
    }

    public BooleanExpression createCouponPredicate(String couponCode, Boolean isRedeemed, String couponTopicName) {
        QCoupon c = QCoupon.coupon;
        QCouponTopic ct = QCouponTopic.couponTopic;

        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (couponTopicName != null) {
            predicate = predicate.and(ct.name.eq(couponTopicName));
        }
        if (couponCode != null) {
            predicate = predicate.and(c.code.eq(couponCode));
        }
        if (isRedeemed != null) {
            predicate = predicate.and(c.isRedeemed.eq(isRedeemed));
        }

        return predicate;
    }
}
