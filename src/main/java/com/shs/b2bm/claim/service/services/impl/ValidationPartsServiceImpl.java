package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ErrorValidation;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Parts. */
@Service
public class ValidationPartsServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationPartsServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
    super(ruleValidationConfigService);
  }

  @Override
  public String getValidationRule() {
    return Rule.PartsValidation.getDescription();
  }

  @Override
  public ErrorValidation executeValidation(
      ServiceOrder serviceOrder,
      RuleValidationConfig ruleValidationConfig,
      ExtractValueFromJson extractValueFromJson) {
    ErrorValidation errorValidation = new ErrorValidation();

    int maxPartsPerClaim = extractValueFromJson.getIntRule("maxPartsPerClaim", 1);

    if (serviceOrder.getClaims().stream().anyMatch(c -> c.getParts().size() > maxPartsPerClaim)) {
      errorValidation.setErrorMessage(ruleValidationConfig.getErrorMessage());
    }

    return errorValidation;
  }
}
