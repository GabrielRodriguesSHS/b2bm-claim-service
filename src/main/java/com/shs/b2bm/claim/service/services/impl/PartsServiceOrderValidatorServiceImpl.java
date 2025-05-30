package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.PartRulesDetailsDto;
import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.mappers.RuleValidationParametersJsonMapper;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.services.ServiceOrderRuleValidatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartsServiceOrderValidatorServiceImpl implements ServiceOrderRuleValidatorService {

  private final RuleValidationConfigService ruleValidationConfigService;
  private final RuleValidationParametersJsonMapper serviceAttemptProtoMapper;

  public PartsServiceOrderValidatorServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
    this.ruleValidationConfigService = ruleValidationConfigService;
    this.serviceAttemptProtoMapper = RuleValidationParametersJsonMapper.INSTANCE;
  }

  @Override
  public ServiceOrderValidationResultDto validate(ServiceOrderProto serviceOrder) {
    List<String> errorsList = new ArrayList<String>();

    RuleValidationConfig ruleValidationConfig = this.ruleValidationConfigService.findByRuleIdAndPartnerId(Rule.PartsValidation.ordinal() + 1, null);
    PartRulesDetailsDto partRulesDetailsDto = this.serviceAttemptProtoMapper.jsonToPartRulesDetailsDto(ruleValidationConfig);

      if ((serviceOrder.getServiceAttemptsList().size() > Integer.parseInt(partRulesDetailsDto.minPriceValue().toString()))) {
        errorsList.add(ruleValidationConfig.getErrorMessage());

        return new ServiceOrderValidationResultDto(false, errorsList);
      }

    return new ServiceOrderValidationResultDto(true, errorsList);
  }
}
