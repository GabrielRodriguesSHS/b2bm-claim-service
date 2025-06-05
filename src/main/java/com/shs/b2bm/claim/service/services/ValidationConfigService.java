package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.List;

/** Service interface for accessing rule validation configuration data. */
public interface ValidationConfigService {

  /**
   * Finds the rules validation configuration.
   *
   * @param obligorId the obligor identifier
   * @return the matching RuleValidationConfig, or null if not found
   */
  List<RuleValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId);

  ExtractValueFromJson extractRulesFromValidation(RuleValidationConfig ruleValidationConfig);

  RuleValidationConfig findRuleInList(
      List<RuleValidationConfig> validations, Rule rule, Integer obligorId);
}
