package com.we_assignment.entity.archived;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "archived_coupons")
@Getter
public class ArchivedCoupon {

    @Id
    private UUID id;
    private String code;
    private boolean isRedeemed;
    private boolean isActive;
    private LocalDateTime expiredAt;
    private UUID couponTopicId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private ArchivedCoupon(UUID id, String code, boolean isRedeemed, boolean isActive, LocalDateTime expiredAt,
                           UUID couponTopicId, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.code = code;
        this.isRedeemed = isRedeemed;
        this.isActive = isActive;
        this.expiredAt = expiredAt;
        this.couponTopicId = couponTopicId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ArchivedCoupon of(UUID id, String code, boolean isRedeemed, boolean isActive, LocalDateTime expiredAt,
                                    UUID couponTopicId, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new ArchivedCoupon(id, code, isRedeemed, isActive, expiredAt, couponTopicId, createdAt, updatedAt, deletedAt);
    }

}
