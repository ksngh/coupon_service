package com.we_assignment.service.coupon;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupon.*;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import com.we_assignment.repository.querydsl.CustomCouponRepository;
import com.we_assignment.service.CouponRedemptionService;
import com.we_assignment.util.CouponCodeGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final CustomCouponRepository customCouponRepository;
    private final CouponTopicRepository couponTopicRepository;

    private final CouponRedisService couponRedisService;
    private final CouponRedemptionService couponRedemptionService;

    private final ConcurrentHashMap<String, Boolean> couponMap = new ConcurrentHashMap<>();

    @Transactional
    public void generateCoupon(CouponRequestDto.Create couponRequestDto) {

        CouponTopic couponTopic = couponTopicRepository.findById(couponRequestDto.getCouponTopicId())
                .orElseThrow(CouponTopicNullPointerException::new);
        Set<String> codes = CouponCodeGenerator.generateUniqueCodes(couponRequestDto.getCouponQuantity());
        List<Coupon> coupons = codeToCoupon(codes, couponTopic, couponRequestDto.getExpiredAt());

        couponRepository.saveAll(coupons);

    }

    public List<Coupon> codeToCoupon(Set<String> codes, CouponTopic couponTopic, LocalDateTime expiredAt) {
        return codes.stream()
                .map(code -> Coupon.builder()
                        .id(UUID.randomUUID())
                        .couponTopic(couponTopic)
                        .expiredAt(expiredAt)
                        .code(code)
                        .build())
                .toList();
    }

    public Page<CouponResponseDto> getCoupons(String couponCode, Boolean isRedeemed, String couponTopicName, Pageable pageable) {
        BooleanExpression predicate = customCouponRepository.createCouponPredicate(couponCode, isRedeemed, couponTopicName);
        return customCouponRepository.findAllCouponDetails(predicate, pageable);
    }

    @Transactional
    public void updateCoupon(CouponRequestDto.Update couponRequestDto, UUID couponId) {
        Coupon coupon = updateDtoToCoupon(couponRequestDto, couponId);
        couponRepository.save(coupon);
    }

    public Coupon updateDtoToCoupon(CouponRequestDto.Update couponRequestDto, UUID couponId) {

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
    public void determineActiveness(UUID couponTopicId, boolean isActive) {
        List<Coupon> coupons = customCouponRepository.findAllCouponsByCouponTopicId(couponTopicId);
        coupons.forEach(coupon -> {
            if (isActive) coupon.activate();
            else coupon.inActivate();
        });
        couponRepository.saveAll(coupons);
    }

    public boolean validateCoupon(String couponId) {
        Coupon coupon = couponRepository.findByCode(couponId).orElseThrow(CouponNullPointerException::new);
        if (!coupon.isRedeemed() & coupon.isActive()) {
            return true;
        } else {
            throw new CouponInvalidException();
        }
    }

    // 한 사용자가 동일 토픽에 대한 예외처리 필요
    public boolean validateCouponRedemption(String code) {
        if (couponRedemptionService.getCouponRedemptionByCouponCode(code).isEmpty()) {
            return true;
        } else {
            throw new UsedCouponException();
        }
    }

    public void useCoupon(String code, UserDetails userDetails) {
        if (validateCoupon(code)) {
            if (validateCouponRedemption(code)) {
                Coupon coupon = couponRepository.findByCode(code).orElseThrow(CouponNullPointerException::new);
                couponRedemptionService.createCouponRedemption(coupon, userDetails);
                coupon.useCoupon();
            }
        } else {
            throw new CouponUnavailableException();
        }
    }

    @CircuitBreaker(name = "couponService", fallbackMethod = "useCouponFallback")
    @Transactional
    public void processCoupon(String code, UserDetails userDetails) {
        String lockKey = "coupon:lock:" + code;

        try {
            if (couponRedisService.acquireLock(lockKey, 1000)) {
                useCoupon(code, userDetails);
            } else {
                throw new CouponLockException();
            }
        } finally {
            couponRedisService.releaseLock(lockKey);
        }
    }

    @Transactional
    public void useCouponFallback(String code, Throwable throwable) {
        throwable.printStackTrace();
        log.info("useCouponFallback");
        try {
            couponMap.compute(code, (key, value) -> {
                if (value == null || !value) {
                    Coupon coupon = couponRepository.findByCode(code)
                            .orElseThrow(CouponNullPointerException::new);
                    coupon.useCoupon();
                    couponRepository.save(coupon);
                    return true;
                }
                return value;
            });
        } catch (Exception e) {
            throw new CouponUnavailableException();
        }

    }

}
