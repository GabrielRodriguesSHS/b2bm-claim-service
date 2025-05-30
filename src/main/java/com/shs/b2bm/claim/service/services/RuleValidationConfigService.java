package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;

public interface RuleValidationConfigService {

    RuleValidationConfig findByRuleIdAndPartnerId(Integer ruleId, Integer partnerId);
}
