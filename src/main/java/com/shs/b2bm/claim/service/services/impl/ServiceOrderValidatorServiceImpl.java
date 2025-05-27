package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.exceptions.ServiceOrderProcessingException;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.services.ServiceOrderValidatorService;

public class ServiceOrderValidatorServiceImpl implements ServiceOrderValidatorService {

    @Override
    public void validateMessage(ServiceOrderProto serviceOrder) {
        if (serviceOrder.getOrderNumber() == null || serviceOrder.getOrderNumber().isEmpty()) {
            throw new ServiceOrderProcessingException("Service order number is required");
        }
    }
}
