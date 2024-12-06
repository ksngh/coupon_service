package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.dto.response.UserResponseDto;
import com.we_assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public CustomApiResponse<?> getUserInfo(@PathVariable UUID userId) {
        UserResponseDto userResponseDto = userService.getUserResponseDtoById(userId);
        return CustomApiResponse.ok(userResponseDto);
    }

}
