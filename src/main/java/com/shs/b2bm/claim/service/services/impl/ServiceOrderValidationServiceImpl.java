package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.mappers.ServiceOrderProtoMapper;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import com.shs.b2bm.claim.service.services.ServiceOrderValidationService;
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

  private final List<ValidationStrategyService> listRules;
  private final ServiceOrderProtoMapper serviceOrderProtoMapper;

  public ServiceOrderValidationServiceImpl(List<ValidationStrategyService> listRules) {
    this.listRules = listRules;
    this.serviceOrderProtoMapper = ServiceOrderProtoMapper.INSTANCE;
  }

  @Override
  public void validateMessage(ServiceOrderProto serviceOrderProto) {
    if (serviceOrderProto.getServiceOrderNumber() == null || serviceOrderProto.getServiceOrderNumber().isEmpty()) {
      throw new ValidationException("Service order number is required");
    }

    ServiceOrder serviceOrder = serviceOrderProtoMapper.toEntity(serviceOrderProto);
    ServiceOrderValidationResultDto rulesResult = this.rulesValidator(serviceOrder);

    if (!rulesResult.isValid()) {
      log.debug("Some rule failed.");
      log.debug(rulesResult.errorsList().toString());
    }
  }

  /**
   * Validates the incoming service order message by applying all validation rules.
   *
   * @param serviceOrder The service order to validate
   * @throws ValidationException if any rule fails
   */
  private ServiceOrderValidationResultDto rulesValidator(ServiceOrder serviceOrder) {
    boolean valid = true;
    List<String> errorsList = new ArrayList<>(Collections.emptyList());

    for (ValidationStrategyService rule : listRules) {
      ServiceOrderValidationResultDto ruleResult = rule.validate(serviceOrder, 1);
      if (!ruleResult.isValid()) {
        errorsList.addAll(ruleResult.errorsList());
        valid = false;
      }
    }

    return new ServiceOrderValidationResultDto(valid, errorsList);
  }
}
