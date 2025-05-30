package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.SerialNumberRulesDetailsDto;
import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.enums.Rule;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.mappers.RuleValidationParametersJsonMapper;
import com.shs.b2bm.claim.service.services.RuleValidationConfigService;
import com.shs.b2bm.claim.service.services.ServiceOrderRuleValidatorService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderRuleValidatorService to validate Serial Number. */
@Service
public class SerialNumberServiceOrderValidatorServiceImpl
    implements ServiceOrderRuleValidatorService {

  private final RuleValidationConfigService ruleValidationConfigService;
  private final RuleValidationParametersJsonMapper serviceAttemptProtoMapper;

  public SerialNumberServiceOrderValidatorServiceImpl(
      RuleValidationConfigService ruleValidationConfigService) {
    this.ruleValidationConfigService = ruleValidationConfigService;
    this.serviceAttemptProtoMapper = RuleValidationParametersJsonMapper.INSTANCE;
  }

  @Override
  public ServiceOrderValidationResultDto validate(ServiceOrderProto serviceOrder) {
    List<String> errorsList = new ArrayList<String>();

    RuleValidationConfig ruleValidationConfig =
        this.ruleValidationConfigService.findByRuleIdAndPartnerId(
            Rule.PartsValidation.ordinal() + 1, 1);
    SerialNumberRulesDetailsDto serialNumberRulesDetailsDto =
        this.serviceAttemptProtoMapper.jsonToSerialNumberRulesDetailsDto(ruleValidationConfig);

    if (serviceOrder.getOrderNumber().length() >= serialNumberRulesDetailsDto.minLenght()
        && serviceOrder.getOrderNumber().length() <= serialNumberRulesDetailsDto.maxLenght()) {
      errorsList.add(ruleValidationConfig.getErrorMessage());

      return new ServiceOrderValidationResultDto(false, errorsList);
    }

    return new ServiceOrderValidationResultDto(true, errorsList);
  }
}
