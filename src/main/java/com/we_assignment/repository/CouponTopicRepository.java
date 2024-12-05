package com.we_assignment.repository;

import com.we_assignment.entity.CouponTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponTopicRepository extends JpaRepository<CouponTopic, UUID> {
}
