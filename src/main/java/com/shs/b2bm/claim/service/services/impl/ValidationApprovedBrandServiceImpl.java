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
public class ValidationApprovedBrandServiceImpl extends ValidationStrategyServiceImpl {

    public ValidationApprovedBrandServiceImpl(
            ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
        super(validationConfigService, extractValueFromJson);
    }

    @Override
    public Rule getValidationRule() {
        return Rule.APPROVED_BRAND;
    }

    @Override
    public ValidationResult executeValidation(
            ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

        String brandName =
                (serviceOrder.getMerchandise() != null
                        && serviceOrder.getMerchandise().getBrandName() != null)
                        ? serviceOrder.getMerchandise().getBrandName()
                        : "";

        List<String> listApprovedBrand =
                this.extractValueFromJson.getListOfString(
                        rulesDetails, "listApprovedBrand", Collections.emptyList());

        if (listApprovedBrand.stream().noneMatch(b -> b.equalsIgnoreCase(brandName))) {
            validationResult.setStatus(StatusValidation.Error);
        }

        return validationResult;
    }
}
