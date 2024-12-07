package com.we_assignment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public class CouponRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Create {

        @NotNull(message = "CouponTopic ID는 필수 입력값입니다.")
        private final UUID couponTopicId;

        @NotNull(message = "CouponQuantity는 필수 입력값입니다.")
        @Min(value = 1, message = "CouponQuantity는 0 이상이어야 합니다.")
        private final Integer couponQuantity;
    }

    @Getter
    @AllArgsConstructor
    public static class Update {

        @NotNull(message = "CouponTopic ID는 필수 입력값입니다.")
        private final UUID couponTopicId;

        private String code;
        private boolean isRedeemed;
        private boolean isActive;
        private LocalDateTime expiredAt;

    }

}
