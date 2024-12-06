package com.we_assignment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MASTER("ROLE_MASTER"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private final String role;
}
