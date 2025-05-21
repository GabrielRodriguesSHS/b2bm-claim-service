package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ServiceOrderDataImportAuditInformation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Integration tests for {@link ServiceOrderRepository}. Verifies CRUD operations, auditing, and
 * entity relationships for ServiceOrder.
 */
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ServiceOrderRepositoryTest {

  @Autowired private ServiceOrderRepository repository;

  @Autowired private TestEntityManager entityManager;

  /** Test that a ServiceOrder is persisted correctly. */
  @Test
  void whenSaveServiceOrder_thenPersistedCorrectly() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-12345");
    order.setUnitNumber("UNIT-001");

    // Act
    ServiceOrder savedOrder = repository.save(order);

    // Assert
    assertThat(savedOrder.getServiceOrderId()).isNotNull();
    assertThat(entityManager.find(ServiceOrder.class, savedOrder.getServiceOrderId()))
        .isEqualTo(savedOrder);
  }

  /** Test that a ServiceOrder can be found by its ID. */
  @Test
  void whenFindById_thenReturnServiceOrder() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-12345");
    order.setUnitNumber("UNIT-001");
    Long id = entityManager.persistAndGetId(order, Long.class);
    entityManager.flush();

    // Act
    Optional<ServiceOrder> found = repository.findById(id);

    // Assert
    assertThat(found).isPresent();
    assertThat(found.get().getServiceOrderNumber()).isEqualTo("ORD-12345");
  }

  /** Test that all ServiceOrders can be retrieved from the repository. */
  @Test
  void whenFindAll_thenReturnAllServiceOrders() {
    // Arrange
    ServiceOrder order1 = new ServiceOrder();
    order1.setServiceOrderNumber("ORD-12345");
    order1.setUnitNumber("UNIT-001");

    ServiceOrder order2 = new ServiceOrder();
    order2.setServiceOrderNumber("ORD-67890");
    order2.setUnitNumber("UNIT-002");

    entityManager.persist(order1);
    entityManager.persist(order2);
    entityManager.flush();

    // Act
    List<ServiceOrder> orders = repository.findAll();

    // Assert
    assertThat(orders).hasSize(2);
    assertThat(orders)
        .extracting(ServiceOrder::getServiceOrderNumber)
        .containsExactlyInAnyOrder("ORD-12345", "ORD-67890");
  }

  /** Test that updating a ServiceOrder persists the changes correctly. */
  @Test
  void whenUpdateServiceOrder_thenPersistedCorrectly() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-12345");
    order.setUnitNumber("UNIT-001");
    Long id = entityManager.persistAndGetId(order, Long.class);
    entityManager.flush();

    // Act
    ServiceOrder savedOrder = repository.findById(id).get();
    savedOrder.setServiceOrderNumber("ORD-UPDATED");
    repository.save(savedOrder);

    // Assert
    ServiceOrder updatedOrder = entityManager.find(ServiceOrder.class, id);
    assertThat(updatedOrder.getServiceOrderNumber()).isEqualTo("ORD-UPDATED");
  }

  /** Test that deleting a ServiceOrder removes it from the database. */
  @Test
  void whenDeleteServiceOrder_thenRemoved() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-12345");
    order.setUnitNumber("UNIT-001");
    Long id = entityManager.persistAndGetId(order, Long.class);
    entityManager.flush();

    // Act
    repository.deleteById(id);

    // Assert
    ServiceOrder deletedOrder = entityManager.find(ServiceOrder.class, id);
    assertThat(deletedOrder).isNull();
  }

  /** Test that auditing fields are set when a ServiceOrder is saved. */
  @Test
  void whenServiceOrderSaved_thenAuditingFieldsAreSet() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-AUDIT");
    order.setUnitNumber("UNIT-AUDIT");

    // Act
    ServiceOrder savedOrder = repository.save(order);
    entityManager.flush();

    // Assert
    assertThat(savedOrder.getCreatedDate()).isNotNull();
    assertThat(savedOrder.getModifiedDate()).isNotNull();
    assertThat(savedOrder.getCreatedBy()).isNotNull();
    assertThat(savedOrder.getLastModifiedBy()).isNotNull();
    assertThat(savedOrder.getCreatedBy()).isEqualTo("b2bm-claim-service");
    assertThat(savedOrder.getLastModifiedBy()).isEqualTo("b2bm-claim-service");
  }

  /**
   * Test that the relationship between ServiceOrder and ServiceOrderDataImportAuditInformation is
   * persisted.
   */
  @Test
  void whenServiceOrderSavedWithAuditInfo_thenRelationshipIsPersisted() {
    // Arrange
    ServiceOrderDataImportAuditInformation auditInfo = new ServiceOrderDataImportAuditInformation();
    auditInfo.setGeneratedBy("Test System");
    auditInfo.setTotalNumberOfRecords(100);
    entityManager.persist(auditInfo);

    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-WITH-AUDIT");
    order.setUnitNumber("UNIT-WITH-AUDIT");
    order.setServiceOrderDataImportAuditInformation(auditInfo);

    // Act
    ServiceOrder savedOrder = repository.save(order);
    entityManager.flush();
    entityManager.clear(); // Clear persistence context to force reload

    // Assert
    ServiceOrder fetchedOrder = repository.findById(savedOrder.getServiceOrderId()).orElseThrow();
    assertThat(fetchedOrder.getServiceOrderDataImportAuditInformation()).isNotNull();
    assertThat(
            fetchedOrder
                .getServiceOrderDataImportAuditInformation()
                .getServiceOrderDataImportAuditInformationId())
        .isEqualTo(auditInfo.getServiceOrderDataImportAuditInformationId());
    assertThat(fetchedOrder.getServiceOrderDataImportAuditInformation().getGeneratedBy())
        .isEqualTo("Test System");
  }

  /**
   * Test that cascading works when saving a ServiceOrder with ServiceAttempts and verifies
   * bidirectional relationship.
   */
  @Test
  void whenServiceOrderSavedWithServiceAttempts_thenCascadingWorks() {
    // Arrange
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-WITH-ATTEMPTS");
    order.setUnitNumber("UNIT-WITH-ATTEMPTS");

    ServiceAttempt attempt1 = new ServiceAttempt();
    attempt1.setCallCode("CODE-123");
    attempt1.setTechEmployeeNumber("ABC123");
    attempt1.setCallDate(LocalDate.now());
    attempt1.setServiceOrder(order);

    ServiceAttempt attempt2 = new ServiceAttempt();
    attempt2.setCallCode("CODE-456");
    attempt2.setTechEmployeeNumber("ABC123");
    attempt2.setCallDate(LocalDate.now());
    attempt2.setServiceOrder(order);

    List<ServiceAttempt> attempts = new ArrayList<>();
    attempts.add(attempt1);
    attempts.add(attempt2);
    order.setServiceAttempts(attempts);

    // Act
    ServiceOrder savedOrder = repository.save(order);
    entityManager.flush();
    entityManager.clear(); // Clear persistence context to force reload

    // Assert
    ServiceOrder fetchedOrder = repository.findById(savedOrder.getServiceOrderId()).orElseThrow();
    assertThat(fetchedOrder.getServiceAttempts()).hasSize(2);
    assertThat(fetchedOrder.getServiceAttempts().get(0).getServiceAttemptId()).isNotNull();
    assertThat(fetchedOrder.getServiceAttempts().get(1).getServiceAttemptId()).isNotNull();

    // Verify the relationship is bidirectional
    ServiceAttempt fetchedAttempt =
        entityManager.find(
            ServiceAttempt.class, fetchedOrder.getServiceAttempts().get(0).getServiceAttemptId());
    assertThat(fetchedAttempt.getServiceOrder()).isEqualTo(fetchedOrder);
  }
}
