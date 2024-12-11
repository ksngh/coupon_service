package com.we_assignment.common;

import com.we_assignment.exception.coupon.CouponInvalidException;
import com.we_assignment.exception.coupon.CouponLockException;
import com.we_assignment.exception.coupon.CouponNullPointerException;
import com.we_assignment.exception.coupon.CouponUnavailableException;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.exception.member.MemberNullPointerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNullPointerException.class)
    public CustomApiResponse<String> handleMemberNullPointerException(MemberNullPointerException ex) {
        return CustomApiResponse
                .of(HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        "null");
    }

    @ExceptionHandler(CouponTopicNullPointerException.class)
    public CustomApiResponse<String> handleCouponTopicNullPointerException(CouponTopicNullPointerException ex) {
        return CustomApiResponse
                .of(HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        "null");
    }

    @ExceptionHandler(CouponNullPointerException.class)
    public CustomApiResponse<String> handleCouponNullPointerException(CouponNullPointerException ex) {
        return CustomApiResponse
                .of(HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        "null");
    }

    @ExceptionHandler(CouponLockException.class)
    public CustomApiResponse<String> handleCouponLockException(CouponLockException ex) {
        return CustomApiResponse
                .of(HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        "null");
    }

    @ExceptionHandler(CouponUnavailableException.class)
    public CustomApiResponse<String> handleCouponUnavailableException(CouponUnavailableException ex) {
        return CustomApiResponse
                .of(HttpStatus.SERVICE_UNAVAILABLE,
                        ex.getMessage(),
                        "null");
    }

    @ExceptionHandler(CouponInvalidException.class)
    public CustomApiResponse<String> handleCouponInvalidException(CouponInvalidException ex) {
        return CustomApiResponse
                .of(HttpStatus.SERVICE_UNAVAILABLE,
                        ex.getMessage(),
                        "null");
    }
}
