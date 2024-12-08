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

}