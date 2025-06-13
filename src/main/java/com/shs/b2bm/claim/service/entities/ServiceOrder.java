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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service order entity in the system. Contains information about a specific service
 * order including unit number, service order number, and related service attempts.
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
public class ServiceOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code_id")
    @ToString.Exclude
    private JobCode jobCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchandise_id")
    @ToString.Exclude
    private Merchandise merchandise;

    @NotBlank(message = "Service unit number cannot be blank")
    @Column(nullable = false)
    private String serviceUnitNumber;

    @NotBlank(message = "Service order number cannot be blank")
    @Column(nullable = false)
    private String serviceOrderNumber;

    @Column
    private LocalDate serviceOrderCreatedDate;

    @Column
    private LocalDate serviceOrderClosedDate;

    @Column
    private String statusCode;

    @Column
    private String merchandiseModelNumber;

    @Column
    private String merchandiseSerialNumber;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Claim> claims = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<ValidationResult> validationsResult = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<ServiceOrderPart> parts = new ArrayList<>();

    @Column
    private Integer obligorId; // Just for mocking the initial implementations

    @Column
    private Integer procId; // Just for mocking the initial implementations

    @Column
    private String technicianNotes; // Just for mocking the initial implementations

    @Column
    private String serviceDescriptions; // Just for mocking the initial implementations
}
