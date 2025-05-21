package com.shs.b2bm.claim.service.configs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AuditorAwareImpl}. Verifies the current auditor is set correctly for
 * auditing.
 */
class AuditorAwareImplTest {

  /** Test that the current auditor returns the expected project name. */
  @Test
  void getCurrentAuditor_ShouldReturnProjectName() {
    // Arrange
    AuditorAwareImpl auditorAware = new AuditorAwareImpl();

    // Act
    Optional<String> result = auditorAware.getCurrentAuditor();

    // Assert
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("b2bm-service-order");
  }
}
