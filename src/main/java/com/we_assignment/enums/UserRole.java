package com.we_assignment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    MASTER("ROLE_MASTER"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private final String authority;

    public static class Authority {
        public static final String MASTER = "ROLE_MASTER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String USER = "ROLE_USER";
    }
}
