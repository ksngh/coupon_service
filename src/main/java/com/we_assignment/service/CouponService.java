package com.we_assignment.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.entity.QCoupon;
import com.we_assignment.entity.QCouponTopic;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import com.we_assignment.repository.querydsl.CustomCouponRepository;
import com.we_assignment.util.CouponCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CustomCouponRepository customCouponRepository;
    private final CouponTopicRepository couponTopicRepository;

    @Transactional
    public void generateCoupon(CouponRequestDto.Create couponRequestDto) {

        CouponTopic couponTopic = couponTopicRepository.findById(couponRequestDto.getCouponTopicId())
                .orElseThrow(CouponTopicNullPointerException::new);
        Set<String> codes = CouponCodeGenerator.generateUniqueCodes(couponRequestDto.getCouponQuantity());
        List<Coupon> coupons = codeToCoupon(codes, couponTopic);

        couponRepository.saveAll(coupons);

    }

    public List<Coupon> codeToCoupon(Set<String> codes,CouponTopic couponTopic) {
        return codes.stream()
                .map(code -> Coupon.builder()
                        .id(UUID.randomUUID())
                        .couponTopic(couponTopic)
                        .code(code)
                        .build())
                .toList();
    }

    public Page<CouponResponseDto> getCoupons(String couponCode, Boolean isRedeemed, String couponTopicName, Pageable pageable) {
        BooleanExpression predicate = createCouponPredicate(couponCode, isRedeemed, couponTopicName);
        return customCouponRepository.findAllCouponDetails(predicate, pageable);
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

    @Transactional
    public void updateCoupon(CouponRequestDto.Update couponRequestDto,UUID couponId) {
        Coupon coupon = updateDtoToCoupon(couponRequestDto,couponId);
        couponRepository.save(coupon);
    }

    public Coupon updateDtoToCoupon(CouponRequestDto.Update couponRequestDto,UUID couponId) {

        CouponTopic couponTopic = couponTopicRepository
                .findById(couponRequestDto.getCouponTopicId())
                .orElseThrow(CouponTopicNullPointerException::new);

        return Coupon.builder()
                .couponTopic(couponTopic)
                .id(couponId)
                .code(couponRequestDto.getCode())
                .isActive(couponRequestDto.isActive())
                .isRedeemed(couponRequestDto.isRedeemed())
                .build();
    }

    @Transactional
    public void determineActiveness(UUID couponTopicId, boolean isActive){
        List<Coupon> coupons = customCouponRepository.findAllCouponsByCouponTopicId(couponTopicId);
        coupons.forEach(coupon -> {
            if (isActive) coupon.activate();
            else coupon.inActivate();
        });
        couponRepository.saveAll(coupons);
    }

}
