package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.entities.ValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.repositories.ValidationConfigRepository;
import com.shs.b2bm.claim.service.services.ValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ValidationConfigService for accessing rule validation configuration data. */
@Service
@Slf4j
public class ValidationConfigServiceImpl implements ValidationConfigService {

  private final ValidationConfigRepository validationConfigRepository;
  private final ObjectMapper objectMapper;

  public ValidationConfigServiceImpl(
      ValidationConfigRepository validationConfigRepository, ObjectMapper objectMapper) {
    this.validationConfigRepository = validationConfigRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<ValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId) {
    return validationConfigRepository.findByObligorIdOrObligorIdIsNull(obligorId);
  }

  @Override
  public ExtractValueFromJson extractRulesFromValidation(ValidationConfig validationConfig) {
    Map<String, Object> rules = new HashMap<>();

    if (validationConfig != null && !validationConfig.getRuleDetails().isBlank()) {
      try {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        rules = objectMapper.readValue(validationConfig.getRuleDetails(), typeRef);
      } catch (Exception e) {
        log.error("Failed to parse validation rules: {}", e.getMessage());
      }
    }

    return new ExtractValueFromJson(rules);
  }

  @Override
  public ValidationConfig findRuleInList(
      List<ValidationConfig> validations, Rule rule, Integer obligorId) {
    ValidationConfig validationConfig =
        validations.stream()
            .filter(
                r -> r.getRuleName().equals(rule) && Objects.equals(r.getObligorId(), obligorId))
            .findFirst()
            .orElse(null);

    if (validationConfig == null) {
      validationConfig =
          validations.stream()
              .filter(r -> r.getRuleName().equals(rule) && Objects.equals(r.getObligorId(), null))
              .findFirst()
              .orElse(null);
    }

    return validationConfig;
  }
}
