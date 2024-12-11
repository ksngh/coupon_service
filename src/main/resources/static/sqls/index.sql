CREATE INDEX idx_coupon_code ON coupons(code);
CREATE INDEX idx_coupon_redemption_coupon_fk ON coupon_redemptions (coupon_fk);