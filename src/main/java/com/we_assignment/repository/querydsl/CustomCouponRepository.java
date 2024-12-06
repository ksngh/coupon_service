package com.we_assignment.repository.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.we_assignment.dto.response.CouponResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface CustomCouponRepository {

    Page<CouponResponseDto> findAllCouponDetails(BooleanExpression predicate, Pageable pageable);

}
