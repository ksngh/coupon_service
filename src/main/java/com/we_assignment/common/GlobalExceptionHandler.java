package com.we_assignment.common;

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
                        "등록되지 않은 사용자 입니다.",
                        "null");

    }

}
