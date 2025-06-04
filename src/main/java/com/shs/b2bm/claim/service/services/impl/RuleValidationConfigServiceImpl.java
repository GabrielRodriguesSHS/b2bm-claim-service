package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.repositories.RuleValidationConfigRepository;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of RuleValidationConfigService for accessing rule validation configuration data.
 */
@Service
@Slf4j
public class RuleValidationConfigServiceImpl implements RuleValidationConfigService {

  private final RuleValidationConfigRepository ruleValidationConfigRepository;
  private final ObjectMapper objectMapper;

  public RuleValidationConfigServiceImpl(
      RuleValidationConfigRepository ruleValidationConfigRepository, ObjectMapper objectMapper) {
    this.ruleValidationConfigRepository = ruleValidationConfigRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<RuleValidationConfig> findByRuleIdAndPartnerId(Integer partnerId) {
    List<RuleValidationConfig> listRules = new ArrayList<RuleValidationConfig>();

    listRules.addAll(this.ruleValidationConfigRepository.findByPartnerId(partnerId));
    listRules.addAll(this.ruleValidationConfigRepository.findByPartnerId(null));

    return listRules;
  }

  @Override
  public ExtractValueFromJson getExtractRules(RuleValidationConfig ruleValidationConfig) {
    Map<String, Object> rules = new HashMap<>();

    if (ruleValidationConfig != null
        && ruleValidationConfig.getParametersDetails() != null
        && !ruleValidationConfig.getParametersDetails().trim().isEmpty()) {
      try {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        String json =
            objectMapper.readValue(ruleValidationConfig.getParametersDetails(), String.class);
        rules = objectMapper.readValue(json, typeRef);
      } catch (Exception e) {
        log.error("Failed to parse validation rules: {}", e.getMessage());
      }
    }

    return new ExtractValueFromJson(rules);
  }
}
