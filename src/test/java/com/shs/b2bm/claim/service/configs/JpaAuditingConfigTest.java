package com.shs.b2bm.claim.service.configs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;

/**
 * Unit tests for {@link JpaAuditingConfig}. Verifies the auditor provider bean is configured
 * correctly.
 */
class JpaAuditingConfigTest {

  /** Test that the auditorProvider method returns an instance of AuditorAwareImpl. */
  @Test
  void auditorProvider_ShouldReturnAuditorAwareImpl() {
    // Arrange
    JpaAuditingConfig config = new JpaAuditingConfig();

    // Act
    AuditorAware<String> auditorAware = config.auditorProvider();

    // Assert
    assertThat(auditorAware).isNotNull();
    assertThat(auditorAware).isInstanceOf(AuditorAwareImpl.class);
  }
}
