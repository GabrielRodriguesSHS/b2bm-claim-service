package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link RuleValidationConfig} entity. Provides standard CRUD operations
 * and custom query methods.
 */
@Repository
public interface RuleValidationConfigRepository extends JpaRepository<RuleValidationConfig, Long> {
  RuleValidationConfig findByRuleIdAndPartnerId(Integer ruleId, Integer partnerId);
}
