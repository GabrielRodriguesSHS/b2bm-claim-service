package com.shs.b2bm.claim.service.entities;

import com.shs.b2bm.claim.service.enums.Rule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing the configuration for rule validation. Stores configuration details for
 * validation rules, including associated partner, rule, parameters, and error messages.
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
public class ValidationConfig extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long validationConfigId;

  @Column
  @Enumerated(EnumType.STRING)
  private Rule ruleName;

  /*@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "obligor_id")
  @ToString.Exclude
  private Obligor obligor;*/
  @Column private Integer obligorId; // Just for mocking the initial implementations

  @Column private String ruleDetails;

  @Column private String errorMessage;
}
