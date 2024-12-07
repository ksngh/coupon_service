package com.we_assignment.util;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class RestDocsTestUtils {

    private RestDocsTestUtils() {
    }

    public static RequestFieldsSnippet commonRequestFields() {
        return requestFields(
                fieldWithPath("couponTopicId").type(JsonFieldType.STRING).description("쿠폰 토픽의 고유 ID"),
                fieldWithPath("code").type(JsonFieldType.STRING).optional().description("쿠폰 코드 (선택 사항)"),
                fieldWithPath("isRedeemed").type(JsonFieldType.BOOLEAN).description("쿠폰 사용 여부"),
                fieldWithPath("isActive").type(JsonFieldType.BOOLEAN).description("쿠폰 활성화 여부"),
                fieldWithPath("expiredAt").type(JsonFieldType.STRING).optional().description("쿠폰 만료 날짜 및 시간 (ISO 8601 형식)")
        );
    }

    public static ResponseFieldsSnippet commonResponseFields() {
        return responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태 메시지"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                fieldWithPath("data.message").type(JsonFieldType.STRING).description("커스텀 메시지")
        );
    }
}

