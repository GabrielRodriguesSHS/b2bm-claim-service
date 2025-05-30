package com.shs.b2bm.claim.service.services;

import com.shs.b2bm.claim.service.dtos.ServiceOrderValidationResultDto;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;

public interface ServiceOrderRuleValidatorService {
  ServiceOrderValidationResultDto validate(ServiceOrderProto serviceOrder);
}
