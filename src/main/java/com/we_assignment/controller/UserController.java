package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.UserRequestDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CustomApiResponse<CustomResponseMessage> signup(@RequestBody UserRequestDto.SignUp signupRequestDto) {
        userService.signUp(signupRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("User " + SuccessMessage.CREATE));
    }

}
