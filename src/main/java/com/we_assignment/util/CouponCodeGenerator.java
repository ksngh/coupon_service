package com.we_assignment.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CouponCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 16;

    public static Set<String> generateUniqueCodes(int n) {
        Set<String> uniqueCodes = ConcurrentHashMap.newKeySet();

        while (uniqueCodes.size() < n) {
            StringBuilder code = new StringBuilder();
            ThreadLocalRandom random = ThreadLocalRandom.current();

            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }
            uniqueCodes.add(code.toString());
        }

        return uniqueCodes;
    }
}

