package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.enums.Rule;

/**
 * Service interface for validating service orders against business rules. Provides validation
 * strategies for different business rule types.
 */
public interface ValidationStrategyService {

    /**
     * Retrieves the validation rule type handled by this strategy.
     *
     * @return the Rule enum value representing the validation rule type
     */
    Rule getValidationRule();

    /**
     * Validates the given service order against defined business rules. Performs comprehensive
     * validation checks based on the specific rule type and returns detailed validation results
     * including any errors found.
     *
     * @param serviceOrder the service order to validate, must not be null
     * @return the ValidationResult containing validation status and any error messages
     * @throws IllegalArgumentException if serviceOrder is null
     */
    ValidationResult validate(ServiceOrder serviceOrder);
}
