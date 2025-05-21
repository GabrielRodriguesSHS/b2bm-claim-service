package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceMonetary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for {@link ServiceMonetaryRepository}. These tests verify the CRUD operations
 * and custom queries for service monetary records.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ServiceMonetary Repository Tests")
class ServiceMonetaryRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private ServiceMonetaryRepository serviceMonetaryRepository;

  private ServiceMonetary testServiceMonetary;
  private LocalDate testDate;

  /** Set up test data before each test. */
  @BeforeEach
  void setUp() {
    testDate = LocalDate.now();
    testServiceMonetary =
        ServiceMonetary.builder().typePayment("CREDIT").datePayment(testDate).value1(100.0).build();
  }

  /**
   * Test case for saving and retrieving a ServiceMonetary entity. Verifies that all fields are
   * correctly persisted and retrieved.
   */
  @Test
  @DisplayName("Should save and retrieve service monetary")
  void shouldSaveAndRetrieveServiceMonetary() {
    // Arrange - using testServiceMonetary from setUp

    // Act
    ServiceMonetary savedServiceMonetary = entityManager.persist(testServiceMonetary);
    entityManager.flush();

    // Assert
    Optional<ServiceMonetary> foundServiceMonetary =
        serviceMonetaryRepository.findById(savedServiceMonetary.getId());
    assertThat(foundServiceMonetary)
        .isPresent()
        .hasValueSatisfying(
            sm -> {
              assertThat(sm.getTypePayment()).isEqualTo("CREDIT");
              assertThat(sm.getValue1()).isEqualTo(100.0);
              assertThat(sm.getDatePayment()).isEqualTo(testDate);
            });
  }

  /**
   * Test case for deleting a ServiceMonetary entity. Verifies that the entity is successfully
   * removed from the database.
   */
  @Test
  @DisplayName("Should delete service monetary")
  void shouldDeleteServiceMonetary() {
    // Arrange
    ServiceMonetary savedServiceMonetary = entityManager.persist(testServiceMonetary);
    entityManager.flush();

    // Act
    serviceMonetaryRepository.deleteById(savedServiceMonetary.getId());
    entityManager.flush();

    // Assert
    Optional<ServiceMonetary> foundServiceMonetary =
        serviceMonetaryRepository.findById(savedServiceMonetary.getId());
    assertThat(foundServiceMonetary).isEmpty();
  }

  /**
   * Test case for updating a ServiceMonetary entity. Verifies that the entity's fields are
   * correctly updated in the database.
   */
  @Test
  @DisplayName("Should update service monetary")
  void shouldUpdateServiceMonetary() {
    // Arrange
    ServiceMonetary savedServiceMonetary = entityManager.persist(testServiceMonetary);
    entityManager.flush();

    // Act
    savedServiceMonetary.setValue1(200.0);
    serviceMonetaryRepository.save(savedServiceMonetary);
    entityManager.flush();

    // Assert
    Optional<ServiceMonetary> foundServiceMonetary =
        serviceMonetaryRepository.findById(savedServiceMonetary.getId());
    assertThat(foundServiceMonetary)
        .isPresent()
        .hasValueSatisfying(sm -> assertThat(sm.getValue1()).isEqualTo(200.0));
  }

  /**
   * Test case for finding all ServiceMonetary entities. Verifies that all entities are correctly
   * retrieved from the database.
   */
  @Test
  @DisplayName("Should find all service monetary records")
  void shouldFindAllServiceMonetary() {
    // Arrange
    entityManager.persist(testServiceMonetary);
    ServiceMonetary secondServiceMonetary =
        ServiceMonetary.builder().typePayment("DEBIT").datePayment(testDate).value1(200.0).build();
    entityManager.persist(secondServiceMonetary);
    entityManager.flush();

    // Act
    List<ServiceMonetary> allServiceMonetary = serviceMonetaryRepository.findAll();

    // Assert
    assertThat(allServiceMonetary)
        .hasSize(2)
        .extracting(ServiceMonetary::getTypePayment)
        .containsExactlyInAnyOrder("CREDIT", "DEBIT");
  }
}
