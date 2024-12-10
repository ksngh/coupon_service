package com.we_assignment.entity.archived;


import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "archived_coupon_redemptions")
public class ArchivedCouponRedemption {

    @Id
    private UUID id;
    private UUID userId;
    private UUID couponId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private ArchivedCouponRedemption(UUID id, UUID userId, UUID couponId, LocalDateTime createdAt, LocalDateTime updatedAt,LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ArchivedCouponRedemption of(UUID id, UUID userId, UUID couponId, LocalDateTime createdAt, LocalDateTime updatedAt,LocalDateTime deletedAt) {
        return new ArchivedCouponRedemption(id,userId,couponId,createdAt,updatedAt,deletedAt);
    }

}