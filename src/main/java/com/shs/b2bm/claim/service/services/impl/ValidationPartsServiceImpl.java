package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.RuleValidationConfigDto;
import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Parts. */
@Service
public class ValidationPartsServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationPartsServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
    super(ruleValidationConfigService);
  }

  @Override
  public Integer getValidationId() {
    return Rule.PartsValidation.ordinal() + 1;
  }

  @Override
  public ServiceOrderValidationResultDto executeValidation(ServiceOrder serviceOrder, Integer partnerId, RuleValidationConfigDto ruleValidationConfigDto) {
    List<String> errorsList = new ArrayList<String>();

    int minPriceValue = ruleValidationConfigDto.getIntRule("minPriceValue", 0);

    /*if (serviceOrder.getServiceAttemptsList().size() < minPriceValue) {
      errorsList.add(ruleValidationConfigDto.errorMessage());

      return new ServiceOrderValidationResultDto(false, errorsList);
    }*/

    return new ServiceOrderValidationResultDto(true, errorsList);
  }
}
