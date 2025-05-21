package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.UdfComponent;
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
 * Integration tests for {@link UdfComponentRepository}. These tests verify the CRUD operations and
 * custom queries for UDF components.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UdfComponent Repository Tests")
class UdfComponentRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UdfComponentRepository udfComponentRepository;

  private UdfComponent testUdfComponent;

  /** Set up test data before each test. */
  @BeforeEach
  void setUp() {
    testUdfComponent = UdfComponent.builder().code(123L).charComponent("TEST_COMPONENT").build();
  }

  /**
   * Test case for saving and retrieving a UdfComponent entity. Verifies that all fields are
   * correctly persisted and retrieved.
   */
  @Test
  @DisplayName("Should save and retrieve UDF component")
  void shouldSaveAndRetrieveUdfComponent() {
    // Arrange - using testUdfComponent from setUp

    // Act
    UdfComponent savedUdfComponent = entityManager.persist(testUdfComponent);
    entityManager.flush();

    // Assert
    Optional<UdfComponent> foundUdfComponent =
        udfComponentRepository.findById(savedUdfComponent.getId());
    assertThat(foundUdfComponent)
        .isPresent()
        .hasValueSatisfying(
            udf -> {
              assertThat(udf.getCode()).isEqualTo(123L);
              assertThat(udf.getCharComponent()).isEqualTo("TEST_COMPONENT");
            });
  }

  /**
   * Test case for deleting a UdfComponent entity. Verifies that the entity is successfully removed
   * from the database.
   */
  @Test
  @DisplayName("Should delete UDF component")
  void shouldDeleteUdfComponent() {
    // Arrange
    UdfComponent savedUdfComponent = entityManager.persist(testUdfComponent);
    entityManager.flush();

    // Act
    udfComponentRepository.deleteById(savedUdfComponent.getId());
    entityManager.flush();

    // Assert
    Optional<UdfComponent> foundUdfComponent =
        udfComponentRepository.findById(savedUdfComponent.getId());
    assertThat(foundUdfComponent).isEmpty();
  }

  /**
   * Test case for updating a UdfComponent entity. Verifies that the entity's fields are correctly
   * updated in the database.
   */
  @Test
  @DisplayName("Should update UDF component")
  void shouldUpdateUdfComponent() {
    // Arrange
    UdfComponent savedUdfComponent = entityManager.persist(testUdfComponent);
    entityManager.flush();

    // Act
    savedUdfComponent.setCharComponent("UPDATED_COMPONENT");
    udfComponentRepository.save(savedUdfComponent);
    entityManager.flush();

    // Assert
    Optional<UdfComponent> foundUdfComponent =
        udfComponentRepository.findById(savedUdfComponent.getId());
    assertThat(foundUdfComponent)
        .isPresent()
        .hasValueSatisfying(
            udf -> assertThat(udf.getCharComponent()).isEqualTo("UPDATED_COMPONENT"));
  }

  /**
   * Test case for finding all UdfComponent entities. Verifies that all entities are correctly
   * retrieved from the database.
   */
  @Test
  @DisplayName("Should find all UDF components")
  void shouldFindAllUdfComponents() {
    // Arrange
    entityManager.persist(testUdfComponent);
    UdfComponent secondUdfComponent =
        UdfComponent.builder().code(456L).charComponent("SECOND_TEST_COMPONENT").build();
    entityManager.persist(secondUdfComponent);
    entityManager.flush();

    // Act
    List<UdfComponent> allUdfComponents = udfComponentRepository.findAll();

    // Assert
    assertThat(allUdfComponents)
        .hasSize(2)
        .extracting(UdfComponent::getCharComponent)
        .containsExactlyInAnyOrder("TEST_COMPONENT", "SECOND_TEST_COMPONENT");
  }
}
