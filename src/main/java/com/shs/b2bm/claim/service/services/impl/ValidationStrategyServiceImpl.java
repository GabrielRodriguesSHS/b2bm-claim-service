package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.StatusValidation;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ValidationStrategyServiceImpl implements ValidationStrategyService {

  private final ValidationConfigService validationConfigService;

  public ValidationStrategyServiceImpl(ValidationConfigService validationConfigService) {
    this.validationConfigService = validationConfigService;
  }

  @Override
  public final ValidationResult validate(ServiceOrder serviceOrder) {

    List<RuleValidationConfig> validations =
        validationConfigService.findByObligorIdOrObligorIdIsNull(serviceOrder.getObligorId());

    RuleValidationConfig ruleValidationConfig =
        validationConfigService.findRuleInList(
            validations, getValidationRule(), serviceOrder.getObligorId());
    ExtractValueFromJson extractValueFromJson =
        validationConfigService.extractRulesFromValidation(ruleValidationConfig);

    ValidationResult validationResult = getValidationResult(serviceOrder, ruleValidationConfig);

    return executeValidation(serviceOrder, validationResult, extractValueFromJson);
  }

  protected abstract ValidationResult executeValidation(
      ServiceOrder serviceOrder,
      ValidationResult validationResult,
      ExtractValueFromJson extractValueFromJson);

  private ValidationResult getValidationResult(
      ServiceOrder serviceOrder, RuleValidationConfig ruleValidationConfig) {
    ValidationResult validationResult = new ValidationResult();

    validationResult.setErrorMessage(
        (ruleValidationConfig != null)
            ? ruleValidationConfig.getErrorMessage()
            : "Rule configuration " + getValidationRule().getDescription() + " not found");
    validationResult.setServiceOrder(serviceOrder);
    validationResult.setRules(
        (ruleValidationConfig != null)
            ? ruleValidationConfig.getRuleDetails()
            : "Rule configuration " + getValidationRule().getDescription() + " not found");
    validationResult.setStatus(StatusValidation.Success);

    return validationResult;
  }
}
