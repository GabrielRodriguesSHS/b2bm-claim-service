package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.Parts;
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
 * Integration tests for {@link PartsRepository}. These tests verify the CRUD operations and custom
 * queries for parts.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Parts Repository Tests")
class PartsRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private PartsRepository partsRepository;

  private Parts testParts;

  /** Set up test data before each test. */
  @BeforeEach
  void setUp() {
    testParts =
        Parts.builder().sequenceNumber(1).partCode("ABC123").partDescription("Test Part").build();
  }

  /**
   * Test case for saving and retrieving a Parts entity. Verifies that all fields are correctly
   * persisted and retrieved.
   */
  @Test
  @DisplayName("Should save and retrieve parts")
  void shouldSaveAndRetrieveParts() {
    // Arrange - using testParts from setUp

    // Act
    Parts savedParts = entityManager.persist(testParts);
    entityManager.flush();

    // Assert
    Optional<Parts> foundParts = partsRepository.findById(savedParts.getId());
    assertThat(foundParts)
        .isPresent()
        .hasValueSatisfying(
            p -> {
              assertThat(p.getSequenceNumber()).isEqualTo(1);
              assertThat(p.getPartCode()).isEqualTo("ABC123");
              assertThat(p.getPartDescription()).isEqualTo("Test Part");
            });
  }

  /**
   * Test case for deleting a Parts entity. Verifies that the entity is successfully removed from
   * the database.
   */
  @Test
  @DisplayName("Should delete parts")
  void shouldDeleteParts() {
    // Arrange
    Parts savedParts = entityManager.persist(testParts);
    entityManager.flush();

    // Act
    partsRepository.deleteById(savedParts.getId());
    entityManager.flush();

    // Assert
    Optional<Parts> foundParts = partsRepository.findById(savedParts.getId());
    assertThat(foundParts).isEmpty();
  }

  /**
   * Test case for updating a Parts entity. Verifies that the entity's fields are correctly updated
   * in the database.
   */
  @Test
  @DisplayName("Should update parts")
  void shouldUpdateParts() {
    // Arrange
    Parts savedParts = entityManager.persist(testParts);
    entityManager.flush();

    // Act
    savedParts.setPartDescription("Updated Description");
    partsRepository.save(savedParts);
    entityManager.flush();

    // Assert
    Optional<Parts> foundParts = partsRepository.findById(savedParts.getId());
    assertThat(foundParts)
        .isPresent()
        .hasValueSatisfying(
            p -> assertThat(p.getPartDescription()).isEqualTo("Updated Description"));
  }

  /**
   * Test case for finding all Parts entities. Verifies that all entities are correctly retrieved
   * from the database.
   */
  @Test
  @DisplayName("Should find all parts")
  void shouldFindAllParts() {
    // Arrange
    entityManager.persist(testParts);
    Parts secondPart =
        Parts.builder()
            .sequenceNumber(2)
            .partCode("XYZ789")
            .partDescription("Second Test Part")
            .build();
    entityManager.persist(secondPart);
    entityManager.flush();

    // Act
    List<Parts> allParts = partsRepository.findAll();

    // Assert
    assertThat(allParts)
        .hasSize(2)
        .extracting(Parts::getPartDescription)
        .containsExactlyInAnyOrder("Test Part", "Second Test Part");
  }
}
