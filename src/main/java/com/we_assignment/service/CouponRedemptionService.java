package com.we_assignment.service;

import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponRedemption;
import com.we_assignment.entity.User;
import com.we_assignment.exception.coupon.CouponNullPointerException;
import com.we_assignment.exception.couponredemption.CouponRedemptionNullPointerException;
import com.we_assignment.exception.member.MemberNullPointerException;
import com.we_assignment.repository.jpa.CouponRedemptionRepository;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponRedemptionService {

    private final CouponRedemptionRepository couponRedemptionRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public void createCouponRedemption(Coupon coupon, UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(MemberNullPointerException::new);

        CouponRedemption couponRedemption = CouponRedemption.builder()
                .coupon(coupon)
                .id(UUID.randomUUID())
                .user(user)
                .build();

        couponRedemptionRepository.save(couponRedemption);
    }

    public Optional<CouponRedemption> getCouponRedemptionByCouponCode(String code) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(CouponNullPointerException::new);
        return couponRedemptionRepository.findByCoupon(coupon);
    }
}
