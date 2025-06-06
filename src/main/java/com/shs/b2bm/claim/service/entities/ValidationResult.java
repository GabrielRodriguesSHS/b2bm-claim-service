package com.shs.b2bm.claim.service.entities;

import com.shs.b2bm.claim.service.enums.StatusValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing the result for rule validation. Stores results details for validation rules,
 * including associated serviceOrder, errorMessage, rules, and status.
 */
@Entity
@Table
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long validationResultId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_id")
  @ToString.Exclude
  private ServiceOrder serviceOrder;

  @Column private String errorMessage;

  @Column private String rules;

  @Column
  @Enumerated(EnumType.STRING)
  private StatusValidation status;
}
