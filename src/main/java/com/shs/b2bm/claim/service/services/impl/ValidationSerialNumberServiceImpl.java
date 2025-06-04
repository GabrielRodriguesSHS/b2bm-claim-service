package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ErrorValidation;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Serial Number. */
@Service
@Slf4j
public class ValidationSerialNumberServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationSerialNumberServiceImpl(
      RuleValidationConfigService ruleValidationConfigService) {
    super(ruleValidationConfigService);
  }

  @Override
  public String getValidationRule() {
    return Rule.SerialNumberValidation.getDescription();
  }

  @Override
  public ErrorValidation executeValidation(
      ServiceOrder serviceOrder,
      RuleValidationConfig ruleValidationConfig,
      ExtractValueFromJson extractValueFromJson) {
    ErrorValidation errorValidation = new ErrorValidation();

    int minLength = extractValueFromJson.getIntRule("minLength", 0);
    int maxLength = extractValueFromJson.getIntRule("maxLength", Integer.MAX_VALUE);

    if (serviceOrder.getServiceOrderNumber().length() < minLength
        || serviceOrder.getServiceOrderNumber().length() > maxLength) {
      errorValidation.setErrorMessage(ruleValidationConfig.getErrorMessage());
    }

    return errorValidation;
  }
}
