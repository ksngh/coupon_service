package com.we_assignment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Timestamped {

    @Column(name = "created_at", nullable = true, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
