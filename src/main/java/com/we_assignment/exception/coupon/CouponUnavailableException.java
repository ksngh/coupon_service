package com.we_assignment.exception.coupon;

public class CouponUnavailableException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "쿠폰 시스템 가용 불가";

    public CouponUnavailableException() {
        super(DEFAULT_MESSAGE);
    }

    public CouponUnavailableException(String message) {
        super(message);
    }
}

