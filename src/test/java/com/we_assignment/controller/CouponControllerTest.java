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
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
        mockMvc.perform(get("/api/coupons")
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
                                parameterWithName("couponCode").description("쿠폰 코드 (선택 사항)").optional(),
                                parameterWithName("isRedeemed").description("쿠폰 사용 여부로 필터링 (선택 사항)").optional(),
                                parameterWithName("couponTopicName").description("쿠폰 주제 이름 (선택 사항)").optional(),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("code").description("HTTP 응답 코드"),
                                fieldWithPath("status").description("HTTP 응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.content[].couponCode").description("쿠폰 코드"),
                                fieldWithPath("data.content[].expirationTime").description("쿠폰 만료 시간"),
                                fieldWithPath("data.content[].redeemed").description("쿠폰 사용 여부"),
                                fieldWithPath("data.content[].couponTopicName").description("쿠폰 주제 이름"),
                                fieldWithPath("data.content[].couponTopicDescription").description("쿠폰 주제 설명"),
                                fieldWithPath("data.pageable.sort.empty").description("정렬 조건이 비어 있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").description("정렬 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").description("비정렬 여부"),
                                fieldWithPath("data.pageable.offset").description("데이터셋 시작 위치"),
                                fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
                                fieldWithPath("data.pageable.pageNumber").description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.paged").description("페이지로 응답되었는지 여부"),
                                fieldWithPath("data.pageable.unpaged").description("페이지가 아닌 응답인지 여부"),
                                fieldWithPath("data.last").description("마지막 페이지인지 여부"),
                                fieldWithPath("data.totalElements").description("전체 요소 수"),
                                fieldWithPath("data.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.size").description("페이지 크기"),
                                fieldWithPath("data.number").description("현재 페이지 번호 (0부터 시작)"),
                                fieldWithPath("data.sort.empty").description("정렬 기준이 비어 있는지 여부"),
                                fieldWithPath("data.sort.sorted").description("정렬되었는지 여부"),
                                fieldWithPath("data.sort.unsorted").description("정렬되지 않았는지 여부"),
                                fieldWithPath("data.first").description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("data.empty").description("컨텐츠가 비어 있는지 여부")
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

        mockMvc.perform(put("/api/coupons/{couponId}", UUID.randomUUID())
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

    @Test
    @DisplayName("쿠폰 토픽 활성/비활성화 테스트")
    void inactivateCouponTopic() throws Exception {
        // Mock 서비스 동작 설정
        UUID couponTopicId = UUID.randomUUID();
        boolean activation = false;

        // Mock 서비스가 호출되는지 확인하기 위해 설정
        doNothing().when(couponService).determineActiveness(couponTopicId, activation);

        mockMvc.perform(patch("/api/coupontopics/{couponTopicId}/coupons", couponTopicId)
                        .param("activation", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value("Coupon UPDATE"))
                .andDo(document("activation-coupon-topic",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("couponTopicId").description("쿠폰 토픽의 고유 ID")
                        ),
                        queryParameters(
                                parameterWithName("activation").description("활성화 여부 (true: 활성화, false: 비활성화)").optional()
                        ),
                        commonResponseFields()
                ));

        // 서비스 메서드가 제대로 호출되었는지 검증
        verify(couponService, times(1)).determineActiveness(couponTopicId, activation);
    }
}
