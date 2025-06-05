package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.enums.StatusValidation;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Serial Number. */
@Service
@Slf4j
public class ValidationSerialNumberServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationSerialNumberServiceImpl(ValidationConfigService ruleValidationConfigService) {
    super(ruleValidationConfigService);
  }

  @Override
  public Rule getValidationRule() {
    return Rule.SerialNumberValidation;
  }

  @Override
  public ValidationResult executeValidation(
      ServiceOrder serviceOrder,
      ValidationResult validationResult,
      ExtractValueFromJson extractValueFromJson) {

    int minLength = extractValueFromJson.getIntRule("minLength", 0);
    int maxLength = extractValueFromJson.getIntRule("maxLength", Integer.MAX_VALUE);

    if (serviceOrder.getServiceOrderNumber().length() < minLength
        || serviceOrder.getServiceOrderNumber().length() > maxLength) {
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
