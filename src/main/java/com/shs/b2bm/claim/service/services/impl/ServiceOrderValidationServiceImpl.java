package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.services.ServiceOrderRuleValidatorService;
import com.shs.b2bm.claim.service.services.ServiceOrderValidationService;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderValidatorService for validating ServiceOrderProto messages. */
@Service
public class ServiceOrderValidationServiceImpl implements ServiceOrderValidationService {

  private final List<ServiceOrderRuleValidatorService> listRules;

  /**
   * Constructor for dependency injection of all validation rules.
   *
   * @param listRules List of all ServiceOrderValidationRule beans
   */
  public ServiceOrderValidationServiceImpl(List<ServiceOrderRuleValidatorService> listRules) {
    this.listRules = listRules;
  }

  /**
   * Validates the incoming service order message.
   *
   * @param serviceOrder The service order to validate
   * @throws ValidationException if validation fails
   */
  @Override
  public void validateMessage(ServiceOrderProto serviceOrder) {
    if (serviceOrder.getOrderNumber() == null || serviceOrder.getOrderNumber().isEmpty()) {
      throw new ValidationException("Service order number is required");
    }

    ServiceOrderValidationResultDto rulesResult = this.rulesValidator(serviceOrder);

    System.out.println(rulesResult.toString());

    if (!rulesResult.isValid()) {
      System.out.println(rulesResult.errorsList().toString());
    }
  }

  /**
   * Validates the incoming service order message by applying all validation rules.
   *
   * @param serviceOrder The service order to validate
   * @throws ValidationException if any rule fails
   */
  private ServiceOrderValidationResultDto rulesValidator(ServiceOrderProto serviceOrder) {
    boolean valid = true;
    List<String> errorsList = new ArrayList<>(Collections.emptyList());

    for (ServiceOrderRuleValidatorService rule : listRules) {
      ServiceOrderValidationResultDto ruleResult = rule.validate(serviceOrder);
      if (!ruleResult.isValid()) {
        errorsList.addAll(ruleResult.errorsList());
        valid = false;
      }
    }

    return new ServiceOrderValidationResultDto(valid, errorsList);
  }
}
