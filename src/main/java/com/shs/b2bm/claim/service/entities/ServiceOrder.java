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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents a service order entity in the system. Contains information about a specific service
 * order including unit number, service order number, and related service attempts.
 */
@Entity
@Table(name = "service_order")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long serviceOrderId;

  @NotBlank(message = "Unit number cannot be blank")
  @Column(nullable = false)
  private String unitNumber;

  @NotBlank(message = "Service order number cannot be blank")
  @Column(nullable = false)
  private String serviceOrderNumber;

  @Temporal(TemporalType.DATE)
  private LocalDate closedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_order_data_import_audit_information_id")
  @ToString.Exclude
  private ServiceOrderDataImportAuditInformation serviceOrderDataImportAuditInformation;

  @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<ServiceAttempt> serviceAttempts = new ArrayList<>();

  @OneToMany(mappedBy = "serviceOrderData", cascade = CascadeType.ALL)
  private List<JobCodes> jobCodes = new ArrayList<>();

  @OneToMany(mappedBy = "serviceOrderData", cascade = CascadeType.ALL)
  private List<Parts> parts = new ArrayList<>();

  @OneToMany(mappedBy = "serviceOrderData", cascade = CascadeType.ALL)
  private List<UdfComponent> udfComponents = new ArrayList<>();

  @OneToMany(mappedBy = "serviceOrderData", cascade = CascadeType.ALL)
  private List<ServiceMonetary> serviceMonetary = new ArrayList<>();
}
