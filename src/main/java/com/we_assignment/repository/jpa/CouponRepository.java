package com.we_assignment.repository.jpa;

import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findAllByCouponTopic(CouponTopic couponTopic);

    Optional<Coupon> findByCode(String code);
}
