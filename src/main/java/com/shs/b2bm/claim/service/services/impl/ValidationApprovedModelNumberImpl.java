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

import java.util.Collections;
import java.util.List;

/**
 * Implementation of ServiceOrderRuleValidatorService to validate Parts.
 */
@Service
@Slf4j
public class ValidationApprovedModelNumberImpl extends ValidationStrategyServiceImpl {

    public ValidationApprovedModelNumberImpl(
            ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
        super(validationConfigService, extractValueFromJson);
    }

    @Override
    public Rule getValidationRule() {
        return Rule.APPROVED_MODEL_NUMBER;
    }

    @Override
    public ValidationResult executeValidation(
            ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

        String modelNumber =
                (serviceOrder.getMerchandise() != null
                        && serviceOrder.getMerchandise().getModelNumber() != null)
                        ? serviceOrder.getMerchandise().getModelNumber()
                        : "";

        List<String> listApprovedModelNumber =
                this.extractValueFromJson.getListOfString(
                        rulesDetails, "listApprovedModelNumber", Collections.emptyList());

        if (listApprovedModelNumber.stream().noneMatch(b -> b.equalsIgnoreCase(modelNumber))) {
            validationResult.setStatus(StatusValidation.Error);
        }

        return validationResult;
    }
}
