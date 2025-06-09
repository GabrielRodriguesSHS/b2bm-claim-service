package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
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

  public ValidationPartsServiceImpl(
      ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
    super(validationConfigService, extractValueFromJson);
  }

  @Override
  public Rule getValidationRule() {
    return Rule.PartsValidation;
  }

  @Override
  public ValidationResult executeValidation(
      ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

    int maxPartsPerClaim = this.extractValueFromJson.getInt(rulesDetails, "maxPartsPerClaim", 1);

    if (serviceOrder.getParts().size() > maxPartsPerClaim) {
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
