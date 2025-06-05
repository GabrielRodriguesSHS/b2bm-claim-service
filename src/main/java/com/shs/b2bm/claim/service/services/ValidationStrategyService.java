package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.Rule;

/** Service interface for validating service orders against business rules. */
public interface ValidationStrategyService {
  Rule getValidationRule();

  /**
   * Validates the given service order against defined business rules.
   *
   * @param serviceOrder the service order to validate
   * @return the result of the validation, including errors if any
   */
  ValidationResult validate(ServiceOrder serviceOrder);
}
