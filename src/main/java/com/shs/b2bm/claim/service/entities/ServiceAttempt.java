package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents an attempt made to service a particular order. Contains information about a specific
 * service attempt including call details, technician information, and time tracking.
 */
@Entity
@Table(name = "service_attempt")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAttempt extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long serviceAttemptId;

  @Temporal(TemporalType.DATE)
  private LocalDate callDate;

  @Column private String techEmployeeNumber;

  @Column private String callCode;

  @Column private LocalDateTime startTime;

  @Column private LocalDateTime endTime;

  @Column private Integer transitTimeInMinutes;

  @Column private String techComment1;

  @Column private String techComment2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_id")
  @ToString.Exclude
  private ServiceOrder serviceOrder;
}
