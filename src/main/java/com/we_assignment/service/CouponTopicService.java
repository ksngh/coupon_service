package com.we_assignment.service;

import com.we_assignment.entity.CouponTopic;
import com.we_assignment.entity.Timestamped;
import com.we_assignment.exception.coupontopic.CouponTopicNullPointerException;
import com.we_assignment.repository.jpa.CouponRepository;
import com.we_assignment.repository.jpa.CouponTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
