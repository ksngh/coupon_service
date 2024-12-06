package com.we_assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Coupon extends Timestamped{

    @Id
    private UUID id;

    @Column(name = "code", length = 16, nullable = false, unique = true)
    private String code;

    @Column(name = "is_redeemed", nullable = false)
    private boolean isRedeemed;

    @ManyToOne
    @JoinColumn(name = "coupon_topic_fk", nullable = false)
    private CouponTopic couponTopic;

}
