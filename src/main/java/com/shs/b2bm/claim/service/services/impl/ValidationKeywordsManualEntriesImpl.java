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
public class ValidationKeywordsManualEntriesImpl extends ValidationStrategyServiceImpl {

    public ValidationKeywordsManualEntriesImpl(
            ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
        super(validationConfigService, extractValueFromJson);
    }

    @Override
    public Rule getValidationRule() {
        return Rule.KEYWORDS_MANUAL_ENTRIES;
    }

    @Override
    public ValidationResult executeValidation(
            ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

        String technicianNotes = (serviceOrder.getTechnicianNotes() != null) ? serviceOrder.getTechnicianNotes() : "";
        String serviceDescriptions = (serviceOrder.getServiceDescriptions() != null) ? serviceOrder.getServiceDescriptions() : "";

        List<String> listKeywordsManualEntries =
                this.extractValueFromJson.getListOfString(
                        rulesDetails, "listKeywordsManualEntries", Collections.emptyList());

        List<String> foundWords = listKeywordsManualEntries.stream()
                .filter(w -> (technicianNotes.toLowerCase().contains(w.toLowerCase())
                        || serviceDescriptions.toLowerCase().contains(w.toLowerCase())))
                .toList();

        if (!foundWords.isEmpty()) {
            validationResult.setErrorMessage(validationResult.getErrorMessage().concat(foundWords.toString()));

            validationResult.setStatus(StatusValidation.Error);
        }

        return validationResult;
    }
}
