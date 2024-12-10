package com.we_assignment.repository.jpa;

import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, UUID> {

    Optional<CouponRedemption> findByCoupon(Coupon coupon);
}
