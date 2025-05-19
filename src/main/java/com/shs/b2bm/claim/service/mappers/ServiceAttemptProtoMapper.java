package com.shs.b2bm.claim.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceAttemptProtoMapper {
  ServiceAttemptProtoMapper INSTANCE = Mappers.getMapper(ServiceAttemptProtoMapper.class);


}
