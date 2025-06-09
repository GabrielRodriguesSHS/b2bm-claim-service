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

/** Implementation of ServiceOrderRuleValidatorService to validate Serial Number. */
@Service
@Slf4j
public class ValidationSerialNumberServiceImpl extends ValidationStrategyServiceImpl {

  public ValidationSerialNumberServiceImpl(
      ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
    super(validationConfigService, extractValueFromJson);
  }

  @Override
  public Rule getValidationRule() {
    return Rule.SerialNumberValidation;
  }

  @Override
  public ValidationResult executeValidation(
      ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

    int serialNumberLength =
        (serviceOrder.getMerchandise() != null
                && serviceOrder.getMerchandise().getSerialNumber() != null)
            ? serviceOrder.getMerchandise().getSerialNumber().length()
            : 0;

    boolean isRequired = extractValueFromJson.getBoolean(rulesDetails, "required", true);
    int minLength = extractValueFromJson.getInt(rulesDetails, "minLength", 0);
    int maxLength = extractValueFromJson.getInt(rulesDetails, "maxLength", Integer.MAX_VALUE);

    if ((isRequired && serviceOrder.getMerchandise().getSerialNumber().isBlank())
        || serialNumberLength < minLength
        || serialNumberLength > maxLength) {
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
