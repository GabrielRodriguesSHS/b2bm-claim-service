package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResult;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;

public interface ServiceOrderRuleValidatorService {
  ServiceOrderValidationResult validate(ServiceOrderProto serviceOrder);
}
