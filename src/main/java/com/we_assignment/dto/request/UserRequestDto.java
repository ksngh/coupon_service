package com.we_assignment.dto.request;

import com.we_assignment.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Create{

        private final String username;
        private final String password;
        private final String email;

    }

    @Getter
    @AllArgsConstructor
    public static class Update{

        private final String username;
        private final String password;
        private final String email;
        private final Role role;
    }

}
