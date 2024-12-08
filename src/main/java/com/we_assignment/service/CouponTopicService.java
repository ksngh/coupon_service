package com.we_assignment.service;

import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.dto.response.CouponTopicResponseDto;
import com.we_assignment.entity.CouponTopic;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponTopicService {

    private final CouponTopicRepository couponTopicRepository;

    @Transactional
    public void createCouponTopic(CouponTopicRequestDto.Create couponTopicRequestDto) {
        CouponTopic couponTopic = createDtoToCouponTopic(couponTopicRequestDto);
        couponTopicRepository.save(couponTopic);
    }

    public CouponTopic createDtoToCouponTopic(CouponTopicRequestDto.Create couponTopicRequest) {
        return CouponTopic.builder()
                .id(UUID.randomUUID())
                .name(couponTopicRequest.getName())
                .description(couponTopicRequest.getDescription())
                .build();
    }

    public CouponTopic getCouponTopicById(UUID couponTopicId) {
        return couponTopicRepository.findById(couponTopicId).orElseThrow(CouponTopicNullPointerException::new);
    }

    public CouponTopicResponseDto getCouponTopicResponseDtoById(UUID couponTopicId) {
        CouponTopic couponTopic = getCouponTopicById(couponTopicId);
        return new CouponTopicResponseDto(couponTopic.getName(), couponTopic.getDescription());
    }
}
