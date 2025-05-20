package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.JobCodes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobCodesProtoMapper {
  JobCodesProtoMapper INSTANCE = Mappers.getMapper(JobCodesProtoMapper.class);

  JobCodes toEntity(com.shs.b2bm.claim.service.kafka.proto.JobCodes jobCodes);
}
