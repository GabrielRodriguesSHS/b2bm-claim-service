package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.repositories.RuleValidationConfigRepository;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of RuleValidationConfigService for accessing rule validation configuration data.
 */
@Service
@Slf4j
public class ValidationConfigServiceImpl implements ValidationConfigService {

  private final RuleValidationConfigRepository ruleValidationConfigRepository;
  private final ObjectMapper objectMapper;

  public ValidationConfigServiceImpl(
      RuleValidationConfigRepository ruleValidationConfigRepository, ObjectMapper objectMapper) {
    this.ruleValidationConfigRepository = ruleValidationConfigRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<RuleValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId) {
    return ruleValidationConfigRepository.findByObligorIdOrObligorIdIsNull(obligorId);
  }

  @Override
  public ExtractValueFromJson extractRulesFromValidation(
      RuleValidationConfig ruleValidationConfig) {
    Map<String, Object> rules = new HashMap<>();

    if (ruleValidationConfig != null && !ruleValidationConfig.getRuleDetails().isBlank()) {
      try {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        String json = objectMapper.readValue(ruleValidationConfig.getRuleDetails(), String.class);
        rules = objectMapper.readValue(json, typeRef);
      } catch (Exception e) {
        log.error("Failed to parse validation rules: {}", e.getMessage());
      }
    }

    return new ExtractValueFromJson(rules);
  }

  @Override
  public RuleValidationConfig findRuleInList(
      List<RuleValidationConfig> validations, Rule rule, Integer obligorId) {
    RuleValidationConfig ruleValidationConfig =
        validations.stream()
            .filter(
                r -> r.getRuleName().equals(rule) && Objects.equals(r.getObligorId(), obligorId))
            .findFirst()
            .orElse(null);

    if (ruleValidationConfig == null) {
      ruleValidationConfig =
          validations.stream()
              .filter(r -> r.getRuleName().equals(rule) && Objects.equals(r.getObligorId(), null))
              .findFirst()
              .orElse(null);
    }

    return ruleValidationConfig;
  }
}
