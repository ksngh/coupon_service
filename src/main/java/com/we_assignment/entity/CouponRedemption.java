package com.we_assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupon_redemptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponRedemption extends Timestamped {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_fk", nullable = false)
    private Coupon coupon;

}

