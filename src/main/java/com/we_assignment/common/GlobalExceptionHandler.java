package com.we_assignment.common;

import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.exception.member.MemberNullPointerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
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

}
