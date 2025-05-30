package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.SerialNumberRulesDetailsDto;
import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResult;
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
public class SerialNumberServiceOrderValidatorServiceImpl
    implements ServiceOrderRuleValidatorService {

  private final RuleValidationConfigService ruleValidationConfigService;
  private final RuleValidationParametersJsonMapper serviceAttemptProtoMapper;

  public SerialNumberServiceOrderValidatorServiceImpl(RuleValidationConfigService ruleValidationConfigService) {
    this.ruleValidationConfigService = ruleValidationConfigService;
    this.serviceAttemptProtoMapper = RuleValidationParametersJsonMapper.INSTANCE;
  }

  @Override
  public ServiceOrderValidationResult validate(ServiceOrderProto serviceOrder) {
    List<String> errorsList = new ArrayList<String>();

    RuleValidationConfig ruleValidationConfig = this.ruleValidationConfigService.findByRuleIdAndPartnerId(Rule.PartsValidation.ordinal() + 1, 1);
    SerialNumberRulesDetailsDto serialNumberRulesDetailsDto = this.serviceAttemptProtoMapper.jsonToSerialNumberRulesDetailsDto(ruleValidationConfig);

    if (serviceOrder.getOrderNumber().length() >= serialNumberRulesDetailsDto.minLenght() && serviceOrder.getOrderNumber().length() <= serialNumberRulesDetailsDto.maxLenght()) {
      errorsList.add(ruleValidationConfig.getErrorMessage());

      return new ServiceOrderValidationResult(false, errorsList);
    }

    return new ServiceOrderValidationResult(true, errorsList);
  }
}
