package com.we_assignment.controller;

import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.enums.SuccessMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    @GetMapping()
    public ResponseEntity<?> getAllCoupon() {
        CouponResponseDto couponResponseDto = new CouponResponseDto();
        return ResponseEntity.ok(couponResponseDto);
    }

    @PostMapping()
    public ResponseEntity<?> createCoupons(@RequestBody CouponRequestDto couponRequestDto) {

        return ResponseEntity.ok(new CustomResponseMessage("Coupon" + SuccessMessage.CREATE));
    }

}
