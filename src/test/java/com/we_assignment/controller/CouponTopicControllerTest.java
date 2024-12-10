package com.we_assignment.controller;

import com.we_assignment.common.CustomApiResponse;
import com.we_assignment.common.CustomResponseMessage;
import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.dto.response.CouponTopicResponseDto;
import com.we_assignment.enums.SuccessMessage;
import com.we_assignment.service.CouponTopicService;
import com.we_assignment.util.RestDocsTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.we_assignment.util.ApiDocumentUtils.getDocumentRequest;
import static com.we_assignment.util.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponTopicController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class CouponTopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponTopicService couponTopicService;

    @Test
    @DisplayName("쿠폰 토픽 생성 테스트")
    void createCouponTopic() throws Exception {
        // Mock 데이터 생성
        String requestJson = """
                    {
                        "name": "Holiday Discounts",
                        "description": "Special discounts for the holiday season"
                    }
                """;

        CustomApiResponse<?> mockResponse = CustomApiResponse.ok(
                new CustomResponseMessage("Coupon Topic" + SuccessMessage.CREATE)
        );

        // Service 동작 Mock 설정
        willDoNothing().given(couponTopicService).createCouponTopic(any(CouponTopicRequestDto.Create.class));

        // API 호출 및 문서화
        mockMvc.perform(post("/api/coupontopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andExpect(jsonPath("$.data.message", is("Coupon Topic" + SuccessMessage.CREATE)))
                .andDo(document("create-coupon-topic",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("name").description("쿠폰 토픽 이름"),
                                        fieldWithPath("description").description("쿠폰 토픽 설명")
                                ),
                        RestDocsTestUtils.commonResponseFields()
                        )
                );
    }

    @Test
    @DisplayName("쿠폰 토픽 조회 테스트")
    void getCouponTopic() throws Exception {
        // Mock 데이터 생성
        UUID topicId = UUID.randomUUID();
        CouponTopicResponseDto mockResponseDto = new CouponTopicResponseDto(
                "Holiday Discounts",
                "Special discounts for the holiday season"
        );

        // Service 동작 Mock 설정
        given(couponTopicService.getCouponTopicResponseDtoById(any(UUID.class))).willReturn(mockResponseDto);

        // API 호출 및 문서화
        mockMvc.perform(get("/api/coupontopics/{coupontopicsId}", topicId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andExpect(jsonPath("$.data.name", is("Holiday Discounts")))
                .andExpect(jsonPath("$.data.description", is("Special discounts for the holiday season")))
                .andDo(document("get-coupon-topic",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("coupontopicsId").description("조회할 쿠폰 토픽의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("HTTP 응답 코드"),
                                fieldWithPath("status").description("HTTP 응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.name").description("쿠폰 토픽 이름"),
                                fieldWithPath("data.description").description("쿠폰 토픽 설명")
                        )
                ));
    }

}