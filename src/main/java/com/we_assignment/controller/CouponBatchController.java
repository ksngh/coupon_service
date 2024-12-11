package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.coupon.CouponBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponBatchController {
    private final CouponBatchService couponBatchService;

    @GetMapping("/batch")
    public CustomApiResponse<?> batchControl() throws Exception {
        couponBatchService.runCouponArchiveJob();
        return CustomApiResponse.ok(new CustomResponseMessage("batch" + SuccessMessage.DELETE));
    }
}
