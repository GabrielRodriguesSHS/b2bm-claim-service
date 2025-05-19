package com.shs.b2bm.claim.service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "parts")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Parts extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sequence_number")
  private Integer sequenceNumber;

  @Column(name = "part_code")
  private String partCode;

  @Column(name = "part_description")
  private String partDescription;

  @ManyToOne
  @JoinColumn(name = "service_order_data_id")
  private ServiceOrder serviceOrderData;
}
