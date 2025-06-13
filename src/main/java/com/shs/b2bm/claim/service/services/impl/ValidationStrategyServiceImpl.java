package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationConfig;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.StatusValidation;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class ValidationStrategyServiceImpl implements ValidationStrategyService {

    protected final ExtractValueFromJson extractValueFromJson;
    private final ValidationConfigService validationConfigService;

    public ValidationStrategyServiceImpl(
            ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
        this.validationConfigService = validationConfigService;
        this.extractValueFromJson = extractValueFromJson;
    }

    @Override
    public final ValidationResult validate(ServiceOrder serviceOrder) {

        List<ValidationConfig> validations =
                validationConfigService.findByObligorIdOrObligorIdIsNull(serviceOrder.getObligorId());

        ValidationConfig validationConfig =
                validationConfigService.findRuleInList(
                        validations, getValidationRule(), serviceOrder.getObligorId());

        ValidationResult validationResult = getValidationResult(serviceOrder, validationConfig);

        JsonNode ruleDetails = validationConfigService.extractRuleDetails(validationConfig);

        return executeValidation(serviceOrder, validationResult, ruleDetails);
    }

    protected abstract ValidationResult executeValidation(
            ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails);

    private ValidationResult getValidationResult(
            ServiceOrder serviceOrder, ValidationConfig validationConfig) {
        ValidationResult validationResult = new ValidationResult();

        validationResult.setErrorMessage(
                (validationConfig != null)
                        ? validationConfig.getErrorMessage()
                        : "Rule configuration " + getValidationRule().getDescription() + " not found");
        validationResult.setServiceOrder(serviceOrder);
        validationResult.setRules(
                (validationConfig != null)
                        ? validationConfig.getRuleDetails().toString()
                        : "Rule configuration " + getValidationRule().getDescription() + " not found");
        validationResult.setStatus(StatusValidation.Success);

        return validationResult;
    }
}
