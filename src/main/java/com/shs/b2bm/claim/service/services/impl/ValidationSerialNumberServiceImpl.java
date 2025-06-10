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

    String serialNumber =
        (serviceOrder.getMerchandise() != null
                && serviceOrder.getMerchandise().getSerialNumber() != null)
            ? serviceOrder.getMerchandise().getSerialNumber()
            : "";

    boolean isRequired = extractValueFromJson.getBoolean(rulesDetails, "required", true);
    int minLength = extractValueFromJson.getInt(rulesDetails, "minLength", 0);
    int maxLength = extractValueFromJson.getInt(rulesDetails, "maxLength", Integer.MAX_VALUE);

    if (isRequired && serialNumber.isBlank()) {
      String errorMessageRequired =
          extractValueFromJson.getString(
              rulesDetails, "errorMessageRequired", "Serial missing");
      validationResult.setErrorMessage(
          validationResult.getErrorMessage().concat(" ").concat(errorMessageRequired));
      validationResult.setStatus(StatusValidation.Error);
    }

    if (serialNumber.length() < minLength || serialNumber.length() > maxLength) {
      String errorMessageLength =
          extractValueFromJson.getString(
              rulesDetails, "errorMessageLength", "Serial does not meet length requirements");
      validationResult.setErrorMessage(
          validationResult.getErrorMessage().concat(" ").concat(errorMessageLength));
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
