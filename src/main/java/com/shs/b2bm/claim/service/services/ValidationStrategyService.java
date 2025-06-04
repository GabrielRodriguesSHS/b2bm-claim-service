package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.ErrorValidation;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;

import java.util.List;

/** Service interface for validating service orders against business rules. */
public interface ValidationStrategyService {
  String getValidationRule();

  /**
   * Validates the given service order against defined business rules.
   *
   * @param serviceOrder the service order to validate
   * @return the result of the validation, including errors if any
   */
  ErrorValidation validate(ServiceOrder serviceOrder, Integer partnerId, List<RuleValidationConfig> listRulesConfig);
}
