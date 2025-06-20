package com.shs.b2bm.claim.service.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.shs.b2bm.claim.service.entities.ValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;

import java.util.List;

/**
 * Service interface for accessing rule validation configuration data. Handles retrieval and
 * processing of validation rules configuration for different obligors and business rule types.
 */
public interface ValidationConfigService {

    /**
     * Finds the rule validation configurations for a specific obligor. Returns configurations that
     * match the given obligor ID or are globally applicable (null obligor ID).
     *
     * @param obligorId the obligor identifier to search for configurations, may be null for global
     *                  configs
     * @return a list of matching ValidationConfig objects, never null but may be empty
     */
    List<ValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId);

    /**
     * Finds a specific rule configuration within a list of validation configurations. Searches for a
     * configuration that matches the given rule type and obligor ID, with priority given to
     * obligor-specific configurations over global ones.
     *
     * @param validations the list of validation configurations to search through, must not be null
     * @param rule        the specific rule type to find, must not be null
     * @param obligorId   the obligor identifier for targeted search, may be null for global rules
     * @return the matching ValidationConfig, or null if not found
     * @throws IllegalArgumentException if validations or rule is null
     */
    ValidationConfig findRuleInList(List<ValidationConfig> validations, Rule rule, Integer obligorId);

    /**
     * Extracts rule details from the provided validation configuration. Validates the JsonNode and
     * returns null if it does not exist.
     *
     * @param validationConfig the rule validation configuration to process, must not be null
     * @return ExtractValueFromJson utility object for processing the configuration data
     */
    JsonNode extractRuleDetails(ValidationConfig validationConfig);
}
