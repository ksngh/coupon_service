package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping()
    public ResponseEntity<?> getAllCoupons() {
        CouponResponseDto couponResponseDto = new CouponResponseDto();
        return ResponseEntity.ok(couponResponseDto);
    }

    @PostMapping()
    public CustomApiResponse<?> createCoupons(@RequestBody CouponRequestDto.Create couponRequestDto) {
        couponService.generateCoupon(couponRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon" + SuccessMessage.CREATE));
    }

    @PatchMapping()
    public ResponseEntity<?> updateCoupons(@RequestBody CouponRequestDto couponRequestDto) {
        return ResponseEntity.ok(new CustomResponseMessage("Coupon" + SuccessMessage.UPDATE));
    }

}
