package com.we_assignment.exception.coupon;

public class CouponLockException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "쿠폰 락 획득 실패";

    public CouponLockException() {
        super(DEFAULT_MESSAGE);
    }

    public CouponLockException(String message) {
        super(message);
    }
}

