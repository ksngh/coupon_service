package com.we_assignment.service;

import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.CouponRepository;
import com.we_assignment.repository.CouponTopicRepository;
import com.we_assignment.util.CouponCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponTopicRepository couponTopicRepository;

    public void generateCoupon(CouponRequestDto.Create couponRequestDto) {

        CouponTopic couponTopic = couponTopicRepository.findById(couponRequestDto.getCouponTopicId())
                .orElseThrow(CouponTopicNullPointerException::new);

        Set<String> codes = CouponCodeGenerator.generateUniqueCodes(couponRequestDto.getCouponQuantity());

        List<Coupon> coupons = codes.stream()
                .map(code -> Coupon.builder()
                        .id(UUID.randomUUID())
                        .couponTopic(couponTopic)
                        .code(code)
                        .build())
                .toList(); // Java 16 이상에서는 .toList() 사용 가능, 그렇지 않으면 Collectors.toList() 사용

        couponRepository.saveAll(coupons);

    }



}
