package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "udf_component")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UdfComponent extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code")
  private Long code;

  @Column(name = "char_component")
  private String charComponent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_data_id")
  @ToString.Exclude
  private ServiceOrder serviceOrderData;
}
