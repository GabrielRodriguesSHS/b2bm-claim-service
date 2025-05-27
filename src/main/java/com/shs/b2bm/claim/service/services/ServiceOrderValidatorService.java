package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.exceptions.ServiceOrderProcessingException;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;

/** Service interface for validating ServiceOrderProto messages. */
public interface ServiceOrderValidatorService {

  /**
   * Validates the incoming service order message.
   *
   * @param serviceOrder The service order to validate
   * @throws ServiceOrderProcessingException if validation fails
   */
  void validateMessage(ServiceOrderProto serviceOrder);
}
