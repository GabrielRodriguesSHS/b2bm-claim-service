package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;

/** Service interface for validating service orders against business rules. */
public interface ServiceOrderRuleValidatorService {
  /**
   * Validates the given service order against defined business rules.
   *
   * @param serviceOrder the service order to validate
   * @return the result of the validation, including errors if any
   */
  ServiceOrderValidationResultDto validate(ServiceOrderProto serviceOrder);
}
