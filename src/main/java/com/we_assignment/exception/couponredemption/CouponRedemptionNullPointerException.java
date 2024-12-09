package com.we_assignment.exception.couponredemption;

public class CouponRedemptionNullPointerException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "존재하지 않는 쿠폰 redemption 입니다.";

  public CouponRedemptionNullPointerException() {
    super(DEFAULT_MESSAGE);
  }

  public CouponRedemptionNullPointerException(String message) {
    super(message);
  }
}

