package com.shs.b2bm.claim.service.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.JobCodes;
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
 * Integration tests for {@link JobCodesRepository}. These tests verify the CRUD operations and
 * custom queries for job codes.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("JobCodes Repository Tests")
class JobCodesRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private JobCodesRepository jobCodesRepository;

  private JobCodes testJobCodes;

  /** Set up test data before each test. */
  @BeforeEach
  void setUp() {
    testJobCodes =
        JobCodes.builder().sequenceNumber(1).jobCode(123L).description("Test Job Code").build();
  }

  /**
   * Test case for saving and retrieving a JobCodes entity. Verifies that all fields are correctly
   * persisted and retrieved.
   */
  @Test
  @DisplayName("Should save and retrieve job codes")
  void shouldSaveAndRetrieveJobCodes() {
    // Arrange - using testJobCodes from setUp

    // Act
    JobCodes savedJobCodes = entityManager.persist(testJobCodes);
    entityManager.flush();

    // Assert
    Optional<JobCodes> foundJobCodes = jobCodesRepository.findById(savedJobCodes.getId());
    assertThat(foundJobCodes)
        .isPresent()
        .hasValueSatisfying(
            jc -> {
              assertThat(jc.getSequenceNumber()).isEqualTo(1);
              assertThat(jc.getJobCode()).isEqualTo(123L);
              assertThat(jc.getDescription()).isEqualTo("Test Job Code");
            });
  }

  /**
   * Test case for deleting a JobCodes entity. Verifies that the entity is successfully removed from
   * the database.
   */
  @Test
  @DisplayName("Should delete job codes")
  void shouldDeleteJobCodes() {
    // Arrange
    JobCodes savedJobCodes = entityManager.persist(testJobCodes);
    entityManager.flush();

    // Act
    jobCodesRepository.deleteById(savedJobCodes.getId());
    entityManager.flush();

    // Assert
    Optional<JobCodes> foundJobCodes = jobCodesRepository.findById(savedJobCodes.getId());
    assertThat(foundJobCodes).isEmpty();
  }

  /**
   * Test case for updating a JobCodes entity. Verifies that the entity's fields are correctly
   * updated in the database.
   */
  @Test
  @DisplayName("Should update job codes")
  void shouldUpdateJobCodes() {
    // Arrange
    JobCodes savedJobCodes = entityManager.persist(testJobCodes);
    entityManager.flush();

    // Act
    savedJobCodes.setDescription("Updated Description");
    jobCodesRepository.save(savedJobCodes);
    entityManager.flush();

    // Assert
    Optional<JobCodes> foundJobCodes = jobCodesRepository.findById(savedJobCodes.getId());
    assertThat(foundJobCodes)
        .isPresent()
        .hasValueSatisfying(jc -> assertThat(jc.getDescription()).isEqualTo("Updated Description"));
  }

  /**
   * Test case for finding all JobCodes entities. Verifies that all entities are correctly retrieved
   * from the database.
   */
  @Test
  @DisplayName("Should find all job codes")
  void shouldFindAllJobCodes() {
    // Arrange
    entityManager.persist(testJobCodes);
    JobCodes secondJobCode =
        JobCodes.builder().sequenceNumber(2).jobCode(456L).description("Second Job Code").build();
    entityManager.persist(secondJobCode);
    entityManager.flush();

    // Act
    List<JobCodes> allJobCodes = jobCodesRepository.findAll();

    // Assert
    assertThat(allJobCodes)
        .hasSize(2)
        .extracting(JobCodes::getDescription)
        .containsExactlyInAnyOrder("Test Job Code", "Second Job Code");
  }
}
