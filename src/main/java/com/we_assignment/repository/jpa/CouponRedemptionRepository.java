package com.we_assignment.repository.jpa;

import com.we_assignment.entity.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, UUID> {
}
