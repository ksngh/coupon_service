package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponTopicService;
import com.we_assignment.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponTopicController {

    private final CouponTopicService couponTopicService;

    @DeleteMapping("/coupontopics/{couponTopicId}")
    public CustomApiResponse<?> deleteCouponTopic(@PathVariable UUID couponTopicId) {
        couponTopicService.softDelete(couponTopicId);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.DELETE));
    }
}
