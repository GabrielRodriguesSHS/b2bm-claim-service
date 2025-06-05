package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.enums.StatusValidation;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Parts. */
@Service
@Slf4j
public class ValidationPartsServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationPartsServiceImpl(ValidationConfigService validationConfig) {
    super(validationConfig);
  }

  @Override
  public Rule getValidationRule() {
    return Rule.PartsValidation;
  }

  @Override
  public ValidationResult executeValidation(
      ServiceOrder serviceOrder,
      ValidationResult validationResult,
      ExtractValueFromJson extractValueFromJson) {

    int maxPartsPerClaim = extractValueFromJson.getIntRule("maxPartsPerClaim", 1);

    if (serviceOrder.getClaims().stream().anyMatch(c -> c.getParts().size() > maxPartsPerClaim)) {
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
