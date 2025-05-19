package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.Parts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartsProtoMapper {
  PartsProtoMapper INSTANCE = Mappers.getMapper(PartsProtoMapper.class);

  @Mapping(source = "sequenceNumber", target = "sequenceNumber")
  @Mapping(source = "partCode", target = "partCode")
  @Mapping(source = "partDescription", target = "partDescription")
  Parts toEntity(com.shs.b2bm.claim.service.kafka.proto.Parts parts);
}
