package com.we_assignment.exception.coupon;

public class UsedCouponException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 사용된 쿠폰 입니다.";

    public UsedCouponException() {
        super(DEFAULT_MESSAGE);
    }

    public UsedCouponException(String message) {
        super(message);
    }
}