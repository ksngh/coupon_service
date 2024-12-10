package com.we_assignment.service;

import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import com.we_assignment.repository.querydsl.CustomCouponRepository;
import com.we_assignment.service.coupon.CouponService;
import com.we_assignment.util.CouponCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CustomCouponRepository customCouponRepository;

    @Mock
    private CouponTopicRepository couponTopicRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("쿠폰 생성 성공 테스트")
    void testGenerateCoupon_Success() {
        // Given
        mockStatic(CouponCodeGenerator.class);
        UUID topicId = UUID.randomUUID();
        int quantity = 5;
        CouponTopic couponTopic = CouponTopic.builder()
                .id(topicId)
                .name("Sample Topic")
                .build();

        CouponRequestDto.Create requestDto = new CouponRequestDto.Create(topicId, LocalDateTime.now().plusHours(1),quantity);

        when(couponTopicRepository.findById(topicId)).thenReturn(Optional.of(couponTopic));
        Set<String> generatedCodes = Set.of("AAAABBBBCCCCDDDD", "BBBBCCCCDDDDEEEE", "CCCCDDDDEEEEFFFF", "DDDDEEEEFFFFGGGG", "EEEEFFFFGGGGHHHH");
        when(CouponCodeGenerator.generateUniqueCodes(quantity)).thenReturn(generatedCodes);

        // When
        couponService.generateCoupon(requestDto);

        // Then
        ArgumentCaptor<List<Coupon>> captor = ArgumentCaptor.forClass(List.class);
        verify(couponRepository, times(1)).saveAll(captor.capture());
        List<Coupon> savedCoupons = captor.getValue();

        assertEquals(quantity, savedCoupons.size(), "생성된 쿠폰 개수가 요청한 개수와 같아야 합니다.");
        for (int i = 0; i < quantity; i++) {
            Coupon coupon = savedCoupons.get(i);
            assertNotNull(coupon.getId(), "쿠폰 ID는 null이 아니어야 합니다.");
            assertNotNull(coupon.getCode(), "쿠폰 코드가 null이 아니어야 합니다.");
            assertEquals(couponTopic, coupon.getCouponTopic(), "쿠폰의 주제는 요청한 주제와 동일해야 합니다.");
        }
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰 주제 예외 테스트")
    void testGenerateCoupon_TopicNotFound() {
        // Given
        UUID invalidTopicId = UUID.randomUUID();
        CouponRequestDto.Create requestDto = new CouponRequestDto.Create(invalidTopicId, LocalDateTime.now().plusHours(1),5);

        // Mock 동작 정의
        when(couponTopicRepository.findById(invalidTopicId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CouponTopicNullPointerException.class, () -> couponService.generateCoupon(requestDto),
                "존재하지 않는 쿠폰 주제 ID로 요청 시 CouponTopicNullPointerException이 발생해야 합니다.");
    }

    @Test
    @DisplayName("쿠폰 활성화 테스트")
    void testDetermineActiveness_Activate() {
        // Given
        UUID couponTopicId = UUID.randomUUID();
        CouponTopic couponTopic = CouponTopic.builder()
                .id(couponTopicId)
                .build();

        List<Coupon> coupons = Arrays.asList(
                Coupon.builder()
                        .id(UUID.randomUUID())
                        .couponTopic(couponTopic)
                        .isActive(false) // 초기 상태: 비활성화
                        .build(),
                Coupon.builder()
                        .id(UUID.randomUUID())
                        .couponTopic(couponTopic)
                        .isActive(false) // 초기 상태: 비활성화
                        .build()
        );

        // Mock 설정
        when(customCouponRepository.findAllCouponsByCouponTopicId(couponTopicId)).thenReturn(coupons);

        // When
        couponService.determineActiveness(couponTopicId, true); // 활성화 요청

        // Then
        // 모든 쿠폰이 활성화되었는지 검증
        coupons.stream().map(Coupon::isActive).forEach(System.out::println);
        assertTrue(coupons.stream().allMatch(Coupon::isActive), "모든 쿠폰이 활성화 상태여야 함");

        // Repository 메서드 호출 검증
        verify(customCouponRepository, times(1)).findAllCouponsByCouponTopicId(couponTopicId);
        verify(couponRepository, times(1)).saveAll(coupons);
    }

    @Test
    @DisplayName("쿠폰 비활성화 테스트")
    void testDetermineActiveness_Inactivate() {
        // Given
        UUID couponTopicId = UUID.randomUUID();
        List<Coupon> coupons = Arrays.asList(
                Coupon.builder().id(UUID.randomUUID()).isActive(true).build(),
                Coupon.builder().id(UUID.randomUUID()).isActive(true).build()
        );
        when(customCouponRepository.findAllCouponsByCouponTopicId(couponTopicId)).thenReturn(coupons);

        // When
        couponService.determineActiveness(couponTopicId, false);

        // Then
        // 모든 쿠폰이 비활성화되었는지 검증
        coupons.stream().map(Coupon::isActive).forEach(System.out::println);
        assertTrue(coupons.stream().noneMatch(Coupon::isActive), "모든 쿠폰이 비활성화 상태여야 함");

        // Repository 메서드 호출 검증
        verify(customCouponRepository, times(1)).findAllCouponsByCouponTopicId(couponTopicId);
        verify(couponRepository, times(1)).saveAll(coupons);
    }
}
