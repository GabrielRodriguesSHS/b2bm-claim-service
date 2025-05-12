package com.shs.b2bm.claim.service.configs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class AuditorAwareImplTest {

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
