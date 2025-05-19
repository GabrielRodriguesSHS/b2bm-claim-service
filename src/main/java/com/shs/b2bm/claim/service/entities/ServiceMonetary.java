package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "service_monetary")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMonetary extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type_payment")
  private String typePayment;

  @Column(name = "date_payment")
  private String datePayment;

  @Column(name = "value1")
  private Double value1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_data_id")
  @ToString.Exclude
  private ServiceOrder serviceOrderData;
}
