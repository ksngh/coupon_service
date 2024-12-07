package com.we_assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CouponResponseDto {

    private final String couponCode;
    private final LocalDateTime expirationTime;
    private final boolean isRedeemed;
    private final String couponTopicName;
    private final String couponTopicDescription;

}
