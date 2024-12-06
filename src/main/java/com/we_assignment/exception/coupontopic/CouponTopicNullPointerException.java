package com.we_assignment.exception.coupontopic;

public class CouponTopicNullPointerException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 주제 입니다.";

    public CouponTopicNullPointerException() {
        super(DEFAULT_MESSAGE);
    }

    public CouponTopicNullPointerException(String message) {
        super(message);
    }
}
