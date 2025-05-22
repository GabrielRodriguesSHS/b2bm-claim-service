package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ServiceOrderDataImportAuditInformation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Integration tests for {@link ServiceOrderDataImportAuditInformationRepository}. Verifies CRUD
 * operations, auditing, and entity relationships for ServiceOrderDataImportAuditInformation.
 */
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ServiceOrderDataImportAuditInformationRepositoryTest {

  @Autowired private ServiceOrderDataImportAuditInformationRepository repository;

  @Autowired private TestEntityManager entityManager;

  /** Test that a ServiceOrderDataImportAuditInformation is persisted correctly. */
  @Test
  void whenSaveAuditInformation_thenPersistedCorrectly() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Test");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    // Act
    ServiceOrderDataImportAuditInformation savedAuditInfo = repository.save(auditInfo);

    // Assert
    assertThat(savedAuditInfo.getServiceOrderDataImportAuditInformationId()).isNotNull();
    assertThat(
            entityManager.find(
                ServiceOrderDataImportAuditInformation.class,
                savedAuditInfo.getServiceOrderDataImportAuditInformationId()))
        .isEqualTo(savedAuditInfo);
  }

  /** Test that a ServiceOrderDataImportAuditInformation can be found by its ID. */
  @Test
  void whenFindById_thenReturnAuditInformation() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Test");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    Long id = entityManager.persistAndGetId(auditInfo, Long.class);
    entityManager.flush();

    // Act
    Optional<ServiceOrderDataImportAuditInformation> found = repository.findById(id);

    // Assert
    assertThat(found).isPresent();
    assertThat(found.get().getGeneratedBy()).isEqualTo("System Test");
    assertThat(found.get().getTotalNumberOfRecords()).isEqualTo(100);
  }

  /**
   * Test that all ServiceOrderDataImportAuditInformation entities can be retrieved from the
   * repository.
   */
  @Test
  void whenFindAll_thenReturnAllAuditInformation() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo1 =
        new ServiceOrderDataImportAuditInformation();
    auditInfo1.setGeneratedBy("System Test 1");
    auditInfo1.setCreatedDate(LocalDateTime.now());
    auditInfo1.setModifiedDate(LocalDateTime.now());
    auditInfo1.setCreatedBy("b2bm-claim-service");
    auditInfo1.setLastModifiedBy("b2bm-claim-service");
    auditInfo1.setTotalNumberOfRecords(100);

    ServiceOrderDataImportAuditInformation auditInfo2 =
        new ServiceOrderDataImportAuditInformation();
    auditInfo2.setGeneratedBy("System Test 2");
    auditInfo2.setCreatedDate(LocalDateTime.now());
    auditInfo2.setModifiedDate(LocalDateTime.now());
    auditInfo2.setCreatedBy("b2bm-claim-service");
    auditInfo2.setLastModifiedBy("b2bm-claim-service");
    auditInfo2.setTotalNumberOfRecords(200);

    entityManager.persist(auditInfo1);
    entityManager.persist(auditInfo2);
    entityManager.flush();

    // Act
    List<ServiceOrderDataImportAuditInformation> auditInfoList = repository.findAll();

    // Assert
    assertThat(auditInfoList).hasSize(2);
    assertThat(auditInfoList)
        .extracting(ServiceOrderDataImportAuditInformation::getGeneratedBy)
        .containsExactlyInAnyOrder("System Test 1", "System Test 2");
  }

  /** Test that updating a ServiceOrderDataImportAuditInformation persists the changes correctly. */
  @Test
  void whenUpdateAuditInformation_thenPersistedCorrectly() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Test");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    Long id = entityManager.persistAndGetId(auditInfo, Long.class);
    entityManager.flush();

    // Act
    ServiceOrderDataImportAuditInformation savedAuditInfo = repository.findById(id).get();
    savedAuditInfo.setGeneratedBy("Updated System");
    savedAuditInfo.setTotalNumberOfRecords(150);
    repository.save(savedAuditInfo);

    // Assert
    ServiceOrderDataImportAuditInformation updatedAuditInfo =
        entityManager.find(ServiceOrderDataImportAuditInformation.class, id);
    assertThat(updatedAuditInfo.getGeneratedBy()).isEqualTo("Updated System");
    assertThat(updatedAuditInfo.getTotalNumberOfRecords()).isEqualTo(150);
  }

  /** Test that deleting a ServiceOrderDataImportAuditInformation removes it from the database. */
  @Test
  void whenDeleteAuditInformation_thenRemoved() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Test");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    Long id = entityManager.persistAndGetId(auditInfo, Long.class);
    entityManager.flush();

    // Act
    repository.deleteById(id);

    // Assert
    ServiceOrderDataImportAuditInformation deletedAuditInfo =
        entityManager.find(ServiceOrderDataImportAuditInformation.class, id);
    assertThat(deletedAuditInfo).isNull();
  }

  /** Test that auditing fields are set when a ServiceOrderDataImportAuditInformation is saved. */
  @Test
  void whenAuditInformationSaved_thenAuditingFieldsAreSet() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Audit");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    // Act
    ServiceOrderDataImportAuditInformation savedAuditInfo = repository.save(auditInfo);
    entityManager.flush();

    // Assert
    assertThat(savedAuditInfo.getCreatedDate()).isNotNull();
    assertThat(savedAuditInfo.getModifiedDate()).isNotNull();
    assertThat(savedAuditInfo.getCreatedDate()).isEqualTo(savedAuditInfo.getModifiedDate());
    assertThat(savedAuditInfo.getCreatedBy()).isNotNull();
    assertThat(savedAuditInfo.getLastModifiedBy()).isNotNull();
    assertThat(savedAuditInfo.getCreatedBy()).isEqualTo("b2bm-claim-service");
    assertThat(savedAuditInfo.getLastModifiedBy()).isEqualTo("b2bm-claim-service");
  }

  /**
   * Test that cascading and bidirectional relationships work when saving
   * ServiceOrderDataImportAuditInformation with ServiceOrders.
   */
  @Test
  void whenAuditInformationSavedWithServiceOrders_thenCascadingAndBidirectionalRelationshipWork() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("System Relation");
    auditInfo.setCreatedDate(LocalDateTime.now());
    auditInfo.setModifiedDate(LocalDateTime.now());
    auditInfo.setCreatedBy("b2bm-claim-service");
    auditInfo.setLastModifiedBy("b2bm-claim-service");
    auditInfo.setTotalNumberOfRecords(100);

    ServiceOrder order1 = new ServiceOrder();
    order1.setUnitNumber("UNIT-AUDIT-1");
    order1.setServiceOrderNumber("ORD-AUDIT-1");
    order1.setCreatedDate(LocalDateTime.now());
    order1.setModifiedDate(LocalDateTime.now());
    order1.setCreatedBy("b2bm-claim-service");
    order1.setLastModifiedBy("b2bm-claim-service");
    order1.setServiceOrderDataImportAuditInformation(auditInfo);

    ServiceOrder order2 = new ServiceOrder();
    order2.setUnitNumber("UNIT-AUDIT-2");
    order2.setServiceOrderNumber("ORD-AUDIT-2");
    order2.setCreatedDate(LocalDateTime.now());
    order2.setModifiedDate(LocalDateTime.now());
    order2.setCreatedBy("b2bm-claim-service");
    order2.setLastModifiedBy("b2bm-claim-service");
    order2.setServiceOrderDataImportAuditInformation(auditInfo);

    List<ServiceOrder> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    auditInfo.setServiceOrders(orders);

    // Act
    ServiceOrderDataImportAuditInformation savedAuditInfo = repository.save(auditInfo);
    entityManager.flush();
    entityManager.clear(); // Clear persistence context to force reload

    // Assert
    ServiceOrderDataImportAuditInformation fetchedAuditInfo =
        repository
            .findById(savedAuditInfo.getServiceOrderDataImportAuditInformationId())
            .orElseThrow();

    assertThat(fetchedAuditInfo).isNotNull();
    assertThat(fetchedAuditInfo.getServiceOrders()).hasSize(2);
    assertThat(fetchedAuditInfo.getServiceOrders())
        .extracting(ServiceOrder::getServiceOrderNumber)
        .containsExactlyInAnyOrder("ORD-AUDIT-1", "ORD-AUDIT-2");

    // Verify bidirectional relationship
    ServiceOrder fetchedOrder =
        entityManager.find(
            ServiceOrder.class, fetchedAuditInfo.getServiceOrders().getFirst().getServiceOrderId());
    assertThat(fetchedOrder.getServiceOrderDataImportAuditInformation())
        .isEqualTo(fetchedAuditInfo);
    assertThat(fetchedOrder.getServiceOrderDataImportAuditInformation().getGeneratedBy())
        .isEqualTo("System Relation");
  }
}
