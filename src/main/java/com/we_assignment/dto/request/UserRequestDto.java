package com.we_assignment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    public static class SignIn {

        private final String email;
        private final String password;
    }

}
