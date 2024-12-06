package com.we_assignment.exception.member;

public class MemberNullPointerException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 사용자입니다.";

    public MemberNullPointerException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNullPointerException(String message) {
        super(message);
    }
}
