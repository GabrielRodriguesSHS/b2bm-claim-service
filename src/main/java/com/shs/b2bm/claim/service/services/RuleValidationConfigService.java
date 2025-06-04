package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.dtos.RuleValidationConfigDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;

/** Service interface for accessing rule validation configuration data. */
public interface RuleValidationConfigService {

  /**
   * Finds the rule validation configuration for the given rule and partner IDs.
   *
   * @param ruleId the rule identifier
   * @param partnerId the partner identifier
   * @return the matching RuleValidationConfig, or null if not found
   */
  RuleValidationConfigDto findByRuleIdAndPartnerId(Integer ruleId, Integer partnerId);
}
