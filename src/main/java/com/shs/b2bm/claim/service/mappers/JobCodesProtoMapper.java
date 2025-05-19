package com.shs.b2bm.claim.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobCodesProtoMapper {
  JobCodesProtoMapper INSTANCE = Mappers.getMapper(JobCodesProtoMapper.class);

  @Mapping(source = "sequenceNumber", target = "sequenceNumber")
  @Mapping(source = "jobCode", target = "jobCode")
  @Mapping(source = "description", target = "description")
  com.shs.b2bm.claim.service.entities.JobCodes toEntity(
      com.shs.b2bm.claim.service.kafka.proto.JobCodes jobCodes);
}
