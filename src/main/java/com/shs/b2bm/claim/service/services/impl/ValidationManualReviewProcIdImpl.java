package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.enums.StatusValidation;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Parts. */
@Service
@Slf4j
public class ValidationManualReviewProcIdImpl extends ValidationStrategyServiceImpl {

  public ValidationManualReviewProcIdImpl(
      ValidationConfigService validationConfigService, ExtractValueFromJson extractValueFromJson) {
    super(validationConfigService, extractValueFromJson);
  }

  @Override
  public Rule getValidationRule() {
    return Rule.MANUAL_REVIEW_PROC_ID;
  }

  @Override
  public ValidationResult executeValidation(
      ServiceOrder serviceOrder, ValidationResult validationResult, JsonNode rulesDetails) {

    Integer procId = (serviceOrder.getProcId() != null) ? serviceOrder.getProcId() : 0;

    List<Integer> listManualReviewProcId =
        this.extractValueFromJson.getListOfInteger(
            rulesDetails, "listManualReviewProcId", Collections.emptyList());

    if (listManualReviewProcId.stream().anyMatch(b -> b.equals(procId))) {
      validationResult.setStatus(StatusValidation.Error);
    }

    return validationResult;
  }
}
