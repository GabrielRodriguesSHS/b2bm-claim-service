package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import jakarta.validation.ValidationException;
import java.util.List;

/** Service interface for validating ServiceOrderProto messages. */
public interface ServiceOrderValidationService {

  /**
   * Validates the incoming service order message.
   *
   * @param serviceOrder the service order to validate
   * @throws ValidationException if validation fails
   */
  void validateMessage(ServiceOrderProto serviceOrder, List<RuleValidationConfig> listRules);
}
