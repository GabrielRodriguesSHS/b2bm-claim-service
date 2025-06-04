package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.dtos.RuleValidationConfigDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.repositories.RuleValidationConfigRepository;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of RuleValidationConfigService for accessing rule validation configuration data.
 */
@Service
public class RuleValidationConfigServiceImpl implements RuleValidationConfigService {

  private final RuleValidationConfigRepository ruleValidationConfigRepository;
  private final ObjectMapper objectMapper;

  public RuleValidationConfigServiceImpl(
      RuleValidationConfigRepository ruleValidationConfigRepository, ObjectMapper objectMapper) {
    this.ruleValidationConfigRepository = ruleValidationConfigRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public RuleValidationConfigDto findByRuleIdAndPartnerId(Integer ruleId, Integer partnerId) {
    Optional<RuleValidationConfig> partnerRuleValidationConfig =  this.ruleValidationConfigRepository.findByRuleIdAndPartnerId(ruleId, partnerId);

    if (partnerRuleValidationConfig.isPresent()) {
      return toRuleValidationConfigDto(partnerRuleValidationConfig.get());
    }

    Optional<RuleValidationConfig> globalRuleValidationConfig =  this.ruleValidationConfigRepository.findByRuleIdAndPartnerId(ruleId, null);

    if (globalRuleValidationConfig.isPresent()) {
      return toRuleValidationConfigDto(globalRuleValidationConfig.get());
    }

    return RuleValidationConfigDto.defaultConfig();
  }

  private RuleValidationConfigDto toRuleValidationConfigDto(RuleValidationConfig ruleValidationConfig) {
    Map<String, Object> rules = new HashMap<>();

    if (ruleValidationConfig.getParametersDetails() != null && !ruleValidationConfig.getParametersDetails().trim().isEmpty()) {
      try {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        String json = objectMapper.readValue(ruleValidationConfig.getParametersDetails(), String.class);
        rules = objectMapper.readValue(json, typeRef);
      } catch (Exception e) {
        System.err.println("Failed to parse validation rules: " + e.getMessage());
      }
    }

    return new RuleValidationConfigDto(rules, ruleValidationConfig.getErrorMessage());
  }
}
