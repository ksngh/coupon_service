package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/coupons")
    public CustomApiResponse<Page<CouponResponseDto>> getCoupons(
            @RequestParam(required = false) String couponCode,
            @RequestParam(required = false) Boolean isRedeemed,
            @RequestParam(required = false) String couponTopicName,
            Pageable pageable) {

        Page<CouponResponseDto> couponDtos = couponService.getCoupons(couponCode, isRedeemed, couponTopicName, pageable);

        return CustomApiResponse.ok(couponDtos);
    }

    @PostMapping("/coupons")
    public CustomApiResponse<CustomResponseMessage> createCoupons(@Valid @RequestBody CouponRequestDto.Create couponRequestDto) {
        couponService.generateCoupon(couponRequestDto);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.CREATE));
    }

    @PutMapping("/coupons/{couponId}")
    public CustomApiResponse<CustomResponseMessage> updateCoupons(@Valid @RequestBody CouponRequestDto.Update couponRequestDto,
                                              @PathVariable UUID couponId) {
        couponService.updateCoupon(couponRequestDto,couponId);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.UPDATE));
    }

    @PatchMapping("/coupontopics/{couponTopicId}/coupons")
    public CustomApiResponse<CustomResponseMessage> updateCouponTopicActivation(@PathVariable UUID couponTopicId,
                                                 @RequestParam boolean activation) {
        couponService.determineActiveness(couponTopicId, activation);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.UPDATE));
    }

    @PatchMapping("coupons/{couponCode}")
    public CustomApiResponse<CustomResponseMessage> useCoupon(@PathVariable String couponCode,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        couponService.processCoupon(couponCode,userDetails);
        return CustomApiResponse.ok(new CustomResponseMessage("Coupon " + SuccessMessage.UPDATE));
    }

}
