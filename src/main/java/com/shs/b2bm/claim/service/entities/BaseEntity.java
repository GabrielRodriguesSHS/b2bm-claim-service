package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Base entity class that provides auditing fields for all entities. This class is meant to be
 * extended by all entities requiring audit information.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(nullable = false)
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant lastModifiedDate;
}
