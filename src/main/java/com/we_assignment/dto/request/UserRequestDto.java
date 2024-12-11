package com.we_assignment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    public static class SignUp {

        private final String username;
        private final String password;
        private final String email;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자 추가
    public static class SignIn {
        @NotNull
        private String email;

        @NotNull
        private String password;
    }

}
