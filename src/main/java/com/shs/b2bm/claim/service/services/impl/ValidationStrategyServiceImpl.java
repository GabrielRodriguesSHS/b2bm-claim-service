package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ErrorValidation;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import com.shs.b2bm.claim.service.utils.ExtractValueFromJson;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ValidationStrategyServiceImpl implements ValidationStrategyService {

  private final RuleValidationConfigService ruleValidationConfigService;

  public ValidationStrategyServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
    this.ruleValidationConfigService = ruleValidationConfigService;
  }

  @Override
  public final ErrorValidation validate(
      ServiceOrder serviceOrder, Integer partnerId, List<RuleValidationConfig> listRulesConfig) {

    RuleValidationConfig ruleValidationConfig = this.findRuleInList(listRulesConfig, partnerId);
    ExtractValueFromJson extractValueFromJson =
        this.ruleValidationConfigService.getExtractRules(ruleValidationConfig);

    return executeValidation(serviceOrder, ruleValidationConfig, extractValueFromJson);
  }

  protected abstract ErrorValidation executeValidation(
      ServiceOrder serviceOrder,
      RuleValidationConfig ruleValidationConfig,
      ExtractValueFromJson extractValueFromJson);

  private RuleValidationConfig findRuleInList(
      List<RuleValidationConfig> listRulesConfig, Integer partnerId) {
    RuleValidationConfig rule =
        listRulesConfig.stream()
            .filter(
                r ->
                    r.getRule().equals(getValidationRule())
                        && Objects.equals(r.getPartnerId(), partnerId))
            .findFirst()
            .orElse(null);

    if (rule == null) {
      rule =
          listRulesConfig.stream()
              .filter(
                  r ->
                      r.getRule().equals(getValidationRule())
                          && Objects.equals(r.getPartnerId(), null))
              .findFirst()
              .orElse(null);
    }

    return rule;
  }
}
