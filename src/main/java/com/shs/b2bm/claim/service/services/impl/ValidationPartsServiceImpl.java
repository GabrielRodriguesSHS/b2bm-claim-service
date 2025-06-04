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
  public ErrorValidation executeValidation(ServiceOrder serviceOrder, RuleValidationConfig ruleValidationConfig, ExtractValueFromJson extractValueFromJson) {
    ErrorValidation errorValidation = new ErrorValidation();

    int minPriceValue = extractValueFromJson.getIntRule("minPriceValue", 0);

    /*if (serviceOrder.get < minPriceValue) {
      errorsList.add(ruleValidationConfigDto.errorMessage());

      return new ServiceOrderValidationResultDto(false, errorsList);
    }*/

    return errorValidation;
  }
}
