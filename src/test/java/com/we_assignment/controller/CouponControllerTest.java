package com.we_assignment.controller;

import com.we_assignment.dto.response.CouponResponseDto;
import com.we_assignment.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.we_assignment.util.ApiDocumentUtils.getDocumentRequest;
import static com.we_assignment.util.ApiDocumentUtils.getDocumentResponse;
import static com.we_assignment.util.RestDocsTestUtils.commonRequestFields;
import static com.we_assignment.util.RestDocsTestUtils.commonResponseFields;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성 페이지 테스트")
    void getCouponsPage() throws Exception {
        // Mock 데이터 생성
        CouponResponseDto dto = new CouponResponseDto(
                "ABCD1234WXYZ",
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                false,
                "추석 특가 쿠폰",
                "추석을 기념하여 쿠폰을 드립니다!"
        );

        Page<CouponResponseDto> mockPage = new PageImpl<>(
                List.of(dto),
                PageRequest.of(0, 10),
                1
        );

        // Service 동작 Mock 설정
        given(couponService.getCoupons(anyString(), anyBoolean(), anyString(), any()))
                .willReturn(mockPage);

        // API 호출 및 문서화
        mockMvc.perform(get("/coupons")
                        .param("couponCode", "ABC123XYZ")
                        .param("isRedeemed", "false")
                        .param("couponTopicName", "Holiday Discount")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andDo(document("get-coupons",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(
                                parameterWithName("couponCode").description("The code of the coupon (optional)").optional(),
                                parameterWithName("isRedeemed").description("Filter by redeemed status (optional)").optional(),
                                parameterWithName("couponTopicName").description("The topic name of the coupon (optional)").optional(),
                                parameterWithName("page").description("Page number for pagination"),
                                parameterWithName("size").description("Page size for pagination")
                        ),
                        responseFields(
                                fieldWithPath("code").description("HTTP response code"),
                                fieldWithPath("status").description("HTTP response status"),
                                fieldWithPath("message").description("Response message"),
                                fieldWithPath("data.content[].couponCode").description("The code of the coupon"),
                                fieldWithPath("data.content[].expirationTime").description("The expiration time of the coupon"),
                                fieldWithPath("data.content[].redeemed").description("Whether the coupon is redeemed"),
                                fieldWithPath("data.content[].couponTopicName").description("The topic name of the coupon"),
                                fieldWithPath("data.content[].couponTopicDescription").description("The description of the coupon topic"),
                                fieldWithPath("data.pageable.sort.empty").description("Whether the sort is empty"),
                                fieldWithPath("data.pageable.sort.sorted").description("Whether the sort is sorted"),
                                fieldWithPath("data.pageable.sort.unsorted").description("Whether the sort is unsorted"),
                                fieldWithPath("data.pageable.offset").description("Offset from the beginning of the dataset"),
                                fieldWithPath("data.pageable.pageSize").description("The size of the page"),
                                fieldWithPath("data.pageable.pageNumber").description("The current page number"),
                                fieldWithPath("data.pageable.paged").description("Whether the response is paged"),
                                fieldWithPath("data.pageable.unpaged").description("Whether the response is unpaged"),
                                fieldWithPath("data.last").description("Whether this is the last page"),
                                fieldWithPath("data.totalElements").description("Total number of elements"),
                                fieldWithPath("data.totalPages").description("Total number of pages"),
                                fieldWithPath("data.size").description("Size of the page"),
                                fieldWithPath("data.number").description("The current page number (zero-based)"),
                                fieldWithPath("data.sort.empty").description("Whether the sort criteria is empty"),
                                fieldWithPath("data.sort.sorted").description("Whether the data is sorted"),
                                fieldWithPath("data.sort.unsorted").description("Whether the data is unsorted"),
                                fieldWithPath("data.first").description("Whether this is the first page"),
                                fieldWithPath("data.numberOfElements").description("Number of elements on this page"),
                                fieldWithPath("data.empty").description("Whether the content is empty")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 업데이트 테스트")
    void updateCoupons() throws Exception {
        String requestBody = """
                {
                    "couponTopicId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                    "code": "SUMMER2024120700",
                    "isRedeemed": false,
                    "isActive": true,
                    "expiredAt": "2024-12-31T23:59:59"
                }
                """;

        mockMvc.perform(put("/coupons/{couponId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(document("update-coupons",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("couponId").description("업데이트할 쿠폰의 고유 ID")
                        ),
                        commonRequestFields(),
                        commonResponseFields()
                ));
    }
}
