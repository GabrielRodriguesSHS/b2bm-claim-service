package com.shs.b2bm.claim.service.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.repositories.RuleValidationConfigRepository;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import org.springframework.stereotype.Service;

@Service
public class RuleValidationConfigServiceImpl implements RuleValidationConfigService {

    RuleValidationConfigRepository ruleValidationConfigRepository;

    public RuleValidationConfigServiceImpl(RuleValidationConfigRepository ruleValidationConfigRepository, ObjectMapper objectMapper) {
        this.ruleValidationConfigRepository = ruleValidationConfigRepository;
    }

    @Override
    public RuleValidationConfig findByRuleIdAndPartnerId(Integer ruleId, Integer partnerId) {
        return this.ruleValidationConfigRepository.findByRuleIdAndPartnerId(ruleId, partnerId);
    }
}
