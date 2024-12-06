package com.we_assignment.util;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class CouponCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 16; // 쿠폰 코드 길이

    public static Set<String> generateUniqueCodes(int n) {
        Set<String> uniqueCodes = new HashSet<>();
        SecureRandom random = new SecureRandom();

        while (uniqueCodes.size() < n) {
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }
            uniqueCodes.add(code.toString()); // 중복 자동 제거
        }

        return uniqueCodes;
    }
}