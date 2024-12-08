package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.dto.response.CouponTopicResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponTopicController {

    private final CouponTopicService couponTopicService;

    @PostMapping("/coupontopics")
    public CustomApiResponse<?> createTopic(@RequestBody CouponTopicRequestDto.Create couponTopicRequestDto) {
        couponTopicService.createCouponTopic(couponTopicRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon Topic" + SuccessMessage.CREATE));
    }

    @GetMapping("/coupontopics/{coupontopicsId}")
    public CustomApiResponse<?> getCouponTopic(@PathVariable UUID coupontopicsId) {
        CouponTopicResponseDto couponTopicResponseDto = couponTopicService.getCouponTopicResponseDtoById(coupontopicsId);
        return CustomApiResponse.ok(couponTopicResponseDto);
    }

}
