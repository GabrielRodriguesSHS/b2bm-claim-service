package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Claim extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long claimId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_id")
  @ToString.Exclude
  private ServiceOrder serviceOrder;

  @Column private String claimNumber;

  @Column private LocalDate claimDate;

  @Column private String claimStatus;

  @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @ToString.Exclude
  @Builder.Default
  private List<Part> parts = new ArrayList<>();
}
