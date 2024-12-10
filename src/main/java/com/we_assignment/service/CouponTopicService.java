package com.we_assignment.service;

import com.we_assignment.entity.CouponTopic;
import com.we_assignment.entity.Timestamped;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.we_assignment.dto.request.CouponTopicRequestDto;
import com.we_assignment.dto.response.CouponTopicResponseDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponTopicService {

    private final CouponTopicRepository couponTopicRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void softDelete(UUID couponTopicId) {
        CouponTopic couponTopic = couponTopicRepository.findById(couponTopicId).orElseThrow(CouponTopicNullPointerException::new);
        couponTopic.delete();
        couponSoftDelete(couponTopic);
    }

    public void couponSoftDelete(CouponTopic couponTopic) {
        couponRepository.findAllByCouponTopic(couponTopic).forEach(Timestamped::delete);
    }

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
