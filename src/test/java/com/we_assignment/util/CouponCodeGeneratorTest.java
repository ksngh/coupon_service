package com.we_assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CouponCodeGeneratorTest {

    @Test
    @DisplayName("쿠폰 코드 생성 테스트 (100개)")
    void testGenerateUniqueCodes_SizeAndUniqueness() {
        // Given
        int n = 100; // 생성할 쿠폰 개수

        // When
        Set<String> coupons = CouponCodeGenerator.generateUniqueCodes(n);

        // Then
        // 1. 정확한 개수 생성 확인
        assertEquals(n, coupons.size(), "쿠폰 코드의 갯수 : " + n);

        // 2. 각 코드의 길이가 정확히 16인지 확인
        assertTrue(coupons.stream().allMatch(code -> code.length() == 16),
                "쿠폰이 전부 길이가 16");

        // 3. 중복이 없는지 확인
        assertEquals(coupons.size(), coupons.stream().distinct().count(),
                "중복되지 않은 쿠폰 코드");
    }

    @Test
    @DisplayName("쿠폰 코드 생성 테스트 (0개)")
    void testGenerateUniqueCodes_EmptySetForZeroInput() {
        // Given
        int n = 0; // 생성할 쿠폰 개수

        // When
        Set<String> coupons = CouponCodeGenerator.generateUniqueCodes(n);

        // Then
        // 빈 집합인지 확인
        assertTrue(coupons.isEmpty(), "set 원소의 갯수가 0");
    }
}
