package com.we_assignment.exception.coupon;

public class CouponNullPointerException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 쿠폰 입니다.";

    public CouponNullPointerException() {
        super(DEFAULT_MESSAGE);
    }

    public CouponNullPointerException(String message) {
        super(message);
    }
}

