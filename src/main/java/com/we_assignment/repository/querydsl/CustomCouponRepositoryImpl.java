package com.we_assignment.repository.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.entity.QCoupon;
import com.we_assignment.entity.QCouponTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCouponRepositoryImpl implements CustomCouponRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CouponResponseDto> findAllCouponDetails(BooleanExpression predicate, Pageable pageable) {

        QCoupon qCoupon = QCoupon.coupon;
        QCouponTopic qCouponTopic = QCouponTopic.couponTopic;

        // 1. fetchJoin으로 데이터 조회
        List<Tuple> tuples = queryFactory
                .select(qCoupon.code, qCoupon.isRedeemed, qCouponTopic.name, qCouponTopic.description)
                .from(qCoupon)
                .join(qCoupon.couponTopic, qCouponTopic).fetchJoin()
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. Tuple을 CouponResponseDto로 매핑
        List<CouponResponseDto> results = tuples.stream()
                .map(tuple -> new CouponResponseDto(
                        tuple.get(qCoupon.code),
                        tuple.get(qCoupon.expiredAt),
                        Boolean.TRUE.equals(tuple.get(qCoupon.isRedeemed)),
                        tuple.get(qCouponTopic.name),
                        tuple.get(qCouponTopic.description)
                ))
                .toList();

        // 3. 전체 데이터 개수 쿼리
        long totalCount = queryFactory
                .select(qCoupon.count())
                .from(qCoupon)
                .where(predicate)
                .fetchOne();

        // 4. Page 객체 반환
        return new PageImpl<>(results, pageable, totalCount);
    }
}
