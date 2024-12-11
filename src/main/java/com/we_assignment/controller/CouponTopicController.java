package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.we_assignment.dto.response.CouponTopicResponseDto;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponTopicController {

    private final CouponTopicService couponTopicService;

    @DeleteMapping("/coupontopics/{couponTopicId}")
    public CustomApiResponse<CustomResponseMessage> deleteCouponTopic(@PathVariable UUID couponTopicId) {
        couponTopicService.softDelete(couponTopicId);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.DELETE));
    }

    @PostMapping("/coupontopics")
    public CustomApiResponse<CustomResponseMessage> createTopic(@RequestBody CouponTopicRequestDto.Create couponTopicRequestDto) {
        couponTopicService.createCouponTopic(couponTopicRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon Topic" + SuccessMessage.CREATE));
    }

    @GetMapping("/coupontopics/{coupontopicsId}")
    public CustomApiResponse<CouponTopicResponseDto> getCouponTopic(@PathVariable UUID coupontopicsId) {
        CouponTopicResponseDto couponTopicResponseDto = couponTopicService.getCouponTopicResponseDtoById(coupontopicsId);
        return CustomApiResponse.ok(couponTopicResponseDto);
    }

}
