package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.RuleValidationConfigDto;
import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;

public abstract class ValidationStrategyServiceImpl implements ValidationStrategyService {
    private final RuleValidationConfigService ruleValidationConfigService;

    public ValidationStrategyServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
        this.ruleValidationConfigService = ruleValidationConfigService;
    }

    @Override
    public final ServiceOrderValidationResultDto validate(ServiceOrder serviceOrder, Integer partnerId) {
        RuleValidationConfigDto ruleValidationConfigDto = ruleValidationConfigService.findByRuleIdAndPartnerId(getValidationId(), partnerId);

        return executeValidation(serviceOrder, partnerId, ruleValidationConfigDto);
    }

    protected abstract ServiceOrderValidationResultDto executeValidation(ServiceOrder serviceOrder, Integer partnerId, RuleValidationConfigDto ruleValidationConfigDto);
}
