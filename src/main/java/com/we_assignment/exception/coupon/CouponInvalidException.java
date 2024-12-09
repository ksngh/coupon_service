package com.we_assignment.exception.coupon;

public class CouponInvalidException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "유효하지 않는 쿠폰 입니다.";

    public CouponInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public CouponInvalidException(String message) {
        super(message);
    }
}
