package com.we_assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;


class CouponCodeGeneratorTest {

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
