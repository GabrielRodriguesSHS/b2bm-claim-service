package com.shs.b2bm.claim.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UdfComponentProtoMapper {
  UdfComponentProtoMapper INSTANCE = Mappers.getMapper(UdfComponentProtoMapper.class);

  @Mapping(source = "code", target = "code")
  @Mapping(source = "charComponent", target = "charComponent")
  com.shs.b2bm.claim.service.entities.UdfComponent toEntity(
      com.shs.b2bm.claim.service.kafka.proto.UdfComponent udfComponent);
}
