package com.we_assignment.service;

import com.we_assignment.dto.request.CouponRequestDto;
import com.we_assignment.entity.Coupon;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupon.CouponInvalidException;
import com.we_assignment.exception.coupon.CouponNullPointerException;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import com.we_assignment.repository.jpa.UserRepository;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRedemptionService couponRedemptionService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CustomCouponRepository customCouponRepository;

    @Mock
    private CouponTopicRepository couponTopicRepository;

    @Mock
    private UserRepository userRepository;

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

        CouponRequestDto.Create requestDto = new CouponRequestDto.Create(topicId, LocalDateTime.now().plusHours(1), quantity);

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
        CouponRequestDto.Create requestDto = new CouponRequestDto.Create(invalidTopicId, LocalDateTime.now().plusHours(1), 5);

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

    @Test
    @DisplayName("codeToCoupon 메서드 단위 테스트")
    void codeToCouponTest() {
        // Given
        Set<String> codes = Set.of("CODE1", "CODE2", "CODE3");
        CouponTopic couponTopic = CouponTopic.builder()
                .id(UUID.randomUUID())
                .name("Test Topic")
                .build();
        LocalDateTime expiredAt = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When
        List<Coupon> result = couponService.codeToCoupon(codes, couponTopic, expiredAt);

        // Then
        assertThat(result).hasSize(codes.size()); // 반환된 리스트 크기 확인
        assertThat(result).allSatisfy(coupon -> {
            assertThat(coupon.getCouponTopic()).isEqualTo(couponTopic); // 각 쿠폰의 CouponTopic 확인
            assertThat(coupon.getExpiredAt()).isEqualTo(expiredAt);     // 각 쿠폰의 만료일 확인
            assertThat(coupon.getCode()).isIn(codes);                  // 각 쿠폰의 코드 확인
        });
    }

    @Test
    @DisplayName("validateCoupon - 유효한 쿠폰")
    void validateCoupon_ValidCoupon() {
        // Given
        String couponId = "VALID_COUPON";
        Coupon validCoupon = Coupon.builder()
                .code(couponId)
                .isRedeemed(false)
                .isActive(true)
                .build();

        when(couponRepository.findByCode(couponId)).thenReturn(Optional.of(validCoupon));

        // When
        boolean result = couponService.validateCoupon(couponId);

        // Then
        assertTrue(result); // 유효한 쿠폰이므로 true 반환
        verify(couponRepository, times(1)).findByCode(couponId); // findByCode 호출 검증
    }

    @Test
    @DisplayName("validateCoupon - 쿠폰 없음")
    void validateCoupon_CouponNotFound() {
        // Given
        String couponId = "INVALID_COUPON";
        when(couponRepository.findByCode(couponId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> couponService.validateCoupon(couponId))
                .isInstanceOf(CouponNullPointerException.class); // 예외 발생 확인
        verify(couponRepository, times(1)).findByCode(couponId); // findByCode 호출 검증
    }

    @Test
    @DisplayName("validateCoupon - 사용된 쿠폰")
    void validateCoupon_RedeemedCoupon() {
        // Given
        String couponId = "REDEEMED_COUPON";
        Coupon redeemedCoupon = Coupon.builder()
                .code(couponId)
                .isRedeemed(true)
                .isActive(true)
                .build();

        when(couponRepository.findByCode(couponId)).thenReturn(Optional.of(redeemedCoupon));

        // When & Then
        assertThatThrownBy(() -> couponService.validateCoupon(couponId))
                .isInstanceOf(CouponInvalidException.class); // 예외 발생 확인
        verify(couponRepository, times(1)).findByCode(couponId); // findByCode 호출 검증
    }

    @Test
    @DisplayName("validateCoupon - 비활성화된 쿠폰")
    void validateCoupon_InactiveCoupon() {
        // Given
        String couponId = "INACTIVE_COUPON";
        Coupon inactiveCoupon = Coupon.builder()
                .code(couponId)
                .isRedeemed(false)
                .isActive(false)
                .build();

        when(couponRepository.findByCode(couponId)).thenReturn(Optional.of(inactiveCoupon));

        // When & Then
        assertThatThrownBy(() -> couponService.validateCoupon(couponId))
                .isInstanceOf(CouponInvalidException.class); // 예외 발생 확인
        verify(couponRepository, times(1)).findByCode(couponId); // findByCode 호출 검증
    }

    @Test
    @DisplayName("useCoupon - 정상적으로 쿠폰 사용")
    void useCoupon_Success() {
        // Given
        String code = "VALID_COUPON";
        UserDetails userDetails = User.withUsername("testUser").password("testPass").authorities("USER").build();

        // Coupon 객체 생성
        Coupon coupon = Coupon.builder()
                .id(UUID.randomUUID())
                .code(code)
                .isRedeemed(false)
                .isActive(true)
                .build();

        // Mocking: CouponRepository의 findByCode가 Optional.of(coupon)을 반환
        when(couponRepository.findByCode(code)).thenReturn(Optional.of(coupon));

        // Mocking: CouponRedemptionService의 createCouponRedemption 동작 설정
        doNothing().when(couponRedemptionService).createCouponRedemption(any(), any());

        // When
        couponService.useCoupon(code, userDetails);

        // CouponRedemptionService 호출 검증
        verify(couponRedemptionService, times(1)).createCouponRedemption(coupon, userDetails);

    }


}
