package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.ErrorValidation;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.mappers.ServiceOrderProtoMapper;
import com.shs.b2bm.claim.service.services.ServiceOrderValidationService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderValidatorService for validating ServiceOrderProto messages. */
@Service
@Slf4j
public class ServiceOrderValidationServiceImpl implements ServiceOrderValidationService {

  private final List<ValidationStrategyService> listRulesImplementation;
  private final ServiceOrderProtoMapper serviceOrderProtoMapper;

  public ServiceOrderValidationServiceImpl(
      List<ValidationStrategyService> listRulesImplementation) {
    this.listRulesImplementation = listRulesImplementation;
    this.serviceOrderProtoMapper = ServiceOrderProtoMapper.INSTANCE;
  }

  @Override
  public void validateMessage(
      ServiceOrderProto serviceOrderProto, List<RuleValidationConfig> listRulesConfig) {
    if (serviceOrderProto.getServiceOrderNumber() == null
        || serviceOrderProto.getServiceOrderNumber().isEmpty()) {
      throw new ValidationException("Service order number is required");
    }

    ServiceOrder serviceOrder = serviceOrderProtoMapper.toEntity(serviceOrderProto);
    List<ErrorValidation> listErrorValidation = this.rulesValidator(serviceOrder, listRulesConfig);

    if (!listErrorValidation.isEmpty()) {
      log.debug("Some rule failed.");
      log.debug(listErrorValidation.toString());
    }
  }

  /**
   * Validates the incoming service order message by applying all validation rules.
   *
   * @param serviceOrder The service order to validate
   * @throws ValidationException if any rule fails
   */
  private List<ErrorValidation> rulesValidator(
      ServiceOrder serviceOrder, List<RuleValidationConfig> listRulesConfig) {
    List<ErrorValidation> listErrorValidation = new ArrayList<>(Collections.emptyList());

    for (ValidationStrategyService rule : listRulesImplementation) {
      ErrorValidation ruleResult = rule.validate(serviceOrder, 1, listRulesConfig);
      if (ruleResult.getErrorId() != null) {
        listErrorValidation.add(ruleResult);
      }
    }

    return listErrorValidation;
  }
}
