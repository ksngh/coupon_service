package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponTopicController {

    private final CouponTopicService couponTopicService;

    @PostMapping()
    public CustomApiResponse<?> createTopic(@RequestBody CouponTopicRequestDto.Create couponTopicRequestDto) {
        couponTopicService.createCouponTopic(couponTopicRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon Topic" + SuccessMessage.CREATE));
    }
}
