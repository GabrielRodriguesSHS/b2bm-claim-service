package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import com.shs.b2bm.claim.service.kafka.proto.ServiceAttemptProto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ServiceAttemptProtoMapper {
  ServiceAttemptProtoMapper INSTANCE = Mappers.getMapper(ServiceAttemptProtoMapper.class);

  ServiceAttempt toEntity(ServiceAttemptProto serviceAttempt);
}
