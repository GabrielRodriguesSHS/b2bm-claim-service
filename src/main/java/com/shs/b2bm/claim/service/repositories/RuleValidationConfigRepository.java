package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link RuleValidationConfig} entity. Provides standard CRUD operations
 * and custom query methods.
 */
@Repository
public interface RuleValidationConfigRepository extends JpaRepository<RuleValidationConfig, Long> {
  /**
   * Finds all rule validation configurations where obligorId matches the given value or is null.
   * This allows retrieving both partner-specific rules and global rules in a single query.
   *
   * @param obligorId the obligor to search for
   * @return list of rule validation configurations matching the criteria
   */
  List<RuleValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId);
}
