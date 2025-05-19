package com.shs.b2bm.claim.service.entities;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "job_codes")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobCodes extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sequence_number")
  private Integer sequenceNumber;

  @Column(name = "job_code")
  private Long jobCode;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "service_order_data_id")
  private ServiceOrder serviceOrderData;
}
