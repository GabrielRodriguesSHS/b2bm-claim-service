package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ExtractUtils.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceAttemptProtoMapper {
  ServiceAttemptProtoMapper INSTANCE = Mappers.getMapper(ServiceAttemptProtoMapper.class);

  @Mapping(source = "callDate", target = "callDate", qualifiedByName = "stringToDate")
  @Mapping(source = "technicianEmployeeNumber", target = "techEmployeeNumber")
  @Mapping(source = "startTime", target = "startTime", qualifiedByName = "timestampToLocalDateTime")
  @Mapping(source = "endTime", target = "endTime", qualifiedByName = "timestampToLocalDateTime")
  @Mapping(source = "technicianComment1", target = "techComment1")
  @Mapping(source = "technicianComment2", target = "techComment2")
  @Mapping(target = "serviceOrder", ignore = true)
  @Mapping(target = "serviceAttemptId", ignore = true)
  ServiceAttempt toEntity(com.shs.b2bm.claim.service.kafka.proto.ServiceAttempt serviceAttempt);
}
