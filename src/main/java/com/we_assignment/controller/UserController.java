package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.UserRequestDto;
import com.we_assignment.dto.response.UserResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CustomApiResponse<?> signup(@RequestBody UserRequestDto.SignUp signupRequestDto) {
        userService.signUp(signupRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("User "+SuccessMessage.CREATE));
    }

}
