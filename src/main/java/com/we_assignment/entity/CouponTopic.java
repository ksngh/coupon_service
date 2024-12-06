package com.we_assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CouponTopics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponTopic extends Timestamped{

    @Id
    @Column(name = "coupon_topic_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
