package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;

import java.util.List;

/** Service interface for accessing rule validation configuration data. */
public interface RuleValidationConfigService {

  /**
   * Finds the rules validation configuration.
   *
   * @param partnerId the partner identifier
   * @return the matching RuleValidationConfig, or null if not found
   */
  List<RuleValidationConfig> findByRuleIdAndPartnerId(Integer partnerId);

  ExtractValueFromJson getExtractRules(RuleValidationConfig ruleValidationConfig);
}
