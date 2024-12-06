package com.we_assignment.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiResponse<T> {

    private final int code;
    private final HttpStatus status;
    private final String message;
    private final T data;

    private CustomApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new CustomApiResponse<>(httpStatus, message, data);
    }

    public static <T> CustomApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> CustomApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

}