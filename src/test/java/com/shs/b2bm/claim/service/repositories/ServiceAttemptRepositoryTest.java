package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ServiceAttemptRepositoryTest {

  @Autowired private ServiceAttemptRepository repository;

  @Autowired private TestEntityManager entityManager;

  private ServiceOrder createAndPersistServiceOrder() {
    ServiceOrder order = new ServiceOrder();
    order.setServiceOrderNumber("ORD-TEST");
    order.setUnitNumber("UNIT-TEST");
    entityManager.persist(order);
    return order;
  }

  @Test
  void whenSaveServiceAttempt_thenPersistedCorrectly() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-123");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setStartTime(LocalDateTime.now());
    attempt.setServiceOrder(order);

    // Act
    ServiceAttempt savedAttempt = repository.save(attempt);

    // Assert
    assertThat(savedAttempt.getServiceAttemptId()).isNotNull();
    assertThat(entityManager.find(ServiceAttempt.class, savedAttempt.getServiceAttemptId()))
        .isEqualTo(savedAttempt);
  }

  @Test
  void whenFindById_thenReturnServiceAttempt() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-123");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    Long id = entityManager.persistAndGetId(attempt, Long.class);
    entityManager.flush();

    // Act
    Optional<ServiceAttempt> found = repository.findById(id);

    // Assert
    assertThat(found).isPresent();
    assertThat(found.get().getCallCode()).isEqualTo("CODE-123");
    assertThat(found.get().getTechEmployeeNumber()).isEqualTo("ABC123");
  }

  @Test
  void whenFindAll_thenReturnAllServiceAttempts() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt1 = new ServiceAttempt();
    attempt1.setCallCode("CODE-123");
    attempt1.setTechEmployeeNumber("ABC123");
    attempt1.setServiceOrder(order);

    ServiceAttempt attempt2 = new ServiceAttempt();
    attempt2.setCallCode("CODE-456");
    attempt2.setTechEmployeeNumber("ABC123");
    attempt2.setServiceOrder(order);

    entityManager.persist(attempt1);
    entityManager.persist(attempt2);
    entityManager.flush();

    // Act
    List<ServiceAttempt> attempts = repository.findAll();

    // Assert
    assertThat(attempts).hasSize(2);
    assertThat(attempts)
        .extracting(ServiceAttempt::getCallCode)
        .containsExactlyInAnyOrder("CODE-123", "CODE-456");
  }

  @Test
  void whenUpdateServiceAttempt_thenPersistedCorrectly() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-123");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    Long id = entityManager.persistAndGetId(attempt, Long.class);
    entityManager.flush();

    // Act
    ServiceAttempt savedAttempt = repository.findById(id).get();
    savedAttempt.setCallCode("CODE-UPDATED");
    repository.save(savedAttempt);

    // Assert
    ServiceAttempt updatedAttempt = entityManager.find(ServiceAttempt.class, id);
    assertThat(updatedAttempt.getCallCode()).isEqualTo("CODE-UPDATED");
  }

  @Test
  void whenDeleteServiceAttempt_thenRemoved() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-123");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    Long id = entityManager.persistAndGetId(attempt, Long.class);
    entityManager.flush();

    // Act
    repository.deleteById(id);

    // Assert
    ServiceAttempt deletedAttempt = entityManager.find(ServiceAttempt.class, id);
    assertThat(deletedAttempt).isNull();
  }

  @Test
  void whenServiceAttemptSaved_thenAuditingFieldsAreSet() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-AUDIT");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    // Act
    ServiceAttempt savedAttempt = repository.save(attempt);
    entityManager.flush();

    // Assert
    assertThat(savedAttempt.getCreatedDate()).isNotNull();
    assertThat(savedAttempt.getModifiedDate()).isNotNull();
    assertThat(savedAttempt.getCreatedBy()).isNotNull();
    assertThat(savedAttempt.getLastModifiedBy()).isNotNull();
    assertThat(savedAttempt.getCreatedBy()).isEqualTo("b2bm-service-order");
    assertThat(savedAttempt.getLastModifiedBy()).isEqualTo("b2bm-service-order");
  }

  @Test
  void whenServiceAttemptSavedWithServiceOrder_thenBidirectionalRelationshipIsMaintained() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-RELATION");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    // Act
    ServiceAttempt savedAttempt = repository.save(attempt);
    entityManager.flush();
    entityManager.clear(); // Clear persistence context to force reload

    // Assert - Verify many-to-one relationship
    ServiceAttempt fetchedAttempt =
        repository.findById(savedAttempt.getServiceAttemptId()).orElseThrow();
    assertThat(fetchedAttempt.getServiceOrder()).isNotNull();
    assertThat(fetchedAttempt.getServiceOrder().getServiceOrderId())
        .isEqualTo(order.getServiceOrderId());
    assertThat(fetchedAttempt.getServiceOrder().getServiceOrderNumber()).isEqualTo("ORD-TEST");

    // Verify the other side of the relationship (one-to-many)
    ServiceOrder fetchedOrder = entityManager.find(ServiceOrder.class, order.getServiceOrderId());
    assertThat(fetchedOrder.getServiceAttempts()).hasSize(1);
    assertThat(fetchedOrder.getServiceAttempts().getFirst().getServiceAttemptId())
        .isEqualTo(savedAttempt.getServiceAttemptId());
  }

  @Test
  void whenServiceAttemptIsPersisted_thenItCanBeRetrievedWithRepository() {
    // Arrange
    ServiceOrder order = createAndPersistServiceOrder();

    ServiceAttempt attempt = new ServiceAttempt();
    attempt.setCallCode("CODE-RETRIEVE");
    attempt.setTechEmployeeNumber("ABC123");
    attempt.setServiceOrder(order);

    ServiceAttempt savedAttempt = repository.save(attempt);
    entityManager.flush();
    entityManager.clear(); // Detach to ensure we fetch from DB

    // Act
    ServiceAttempt foundAttempt =
        repository.findById(savedAttempt.getServiceAttemptId()).orElseThrow();

    // Assert
    assertThat(foundAttempt).isNotNull();
    assertThat(foundAttempt.getCallCode()).isEqualTo("CODE-RETRIEVE");
    assertThat(foundAttempt.getTechEmployeeNumber()).isEqualTo("ABC123");
    assertThat(foundAttempt.getServiceOrder()).isNotNull();
    assertThat(foundAttempt.getServiceOrder().getServiceOrderId())
        .isEqualTo(order.getServiceOrderId());
  }
}
