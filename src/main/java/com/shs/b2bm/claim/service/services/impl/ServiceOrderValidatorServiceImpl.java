package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.exceptions.ServiceOrderProcessingException;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.services.ServiceOrderValidatorService;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderValidatorService for validating ServiceOrderProto messages. */
@Service
public class ServiceOrderValidatorServiceImpl implements ServiceOrderValidatorService {

  /**
   * Validates the incoming service order message.
   *
   * @param serviceOrder The service order to validate
   * @throws ServiceOrderProcessingException if validation fails
   */
  @Override
  public void validateMessage(ServiceOrderProto serviceOrder) {
    if (serviceOrder.getOrderNumber() == null || serviceOrder.getOrderNumber().isEmpty()) {
      throw new ServiceOrderProcessingException("Service order number is required");
    }
  }
}
