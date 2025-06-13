package com.shs.b2bm.claim.service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration to enable JPA auditing features. This allows automatic tracking of creation and
 * modification dates, as well as creator and modifier, in entity classes.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Provides an instance of AuditorAware to be used by JPA auditing.
     *
     * @return An AuditorAware implementation.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
