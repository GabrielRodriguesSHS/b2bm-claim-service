package com.shs.b2bm.claim.service.configs;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

/**
 * Implementation of AuditorAware to provide a hardcoded auditor value for JPA auditing purposes
 * until a proper authentication system is implemented.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

  private static final String SYSTEM_AUDITOR = "b2bm-claim-service";

  /**
   * Returns a hardcoded auditor value for the application.
   *
   * @return A fixed value representing the system as the auditor.
   */
  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    // Return project name as the auditor until security is implemented
    return Optional.of(SYSTEM_AUDITOR);
  }
}
