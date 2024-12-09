package com.we_assignment.entity;

import com.we_assignment.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Timestamped {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(name = "role", nullable = false)
    private UserRole role;

}
