package com.we_assignment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;


public class CouponTopicRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Create {

        @NotNull(message = "쿠폰 토픽 이름은 필수값입니다.")
        private final String name;

        @NotNull(message = "쿠폰 토픽 설명은 필수값입니다.")
        private final String description;
    }

}
