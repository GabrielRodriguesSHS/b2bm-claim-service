package com.shs.b2bm.claim.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ExtractUtils.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMonetaryProtoMapper {
  ServiceMonetaryProtoMapper INSTANCE = Mappers.getMapper(ServiceMonetaryProtoMapper.class);

  @Mapping(source = "typePayment", target = "typePayment")
  @Mapping(source = "datePayment", target = "datePayment", qualifiedByName = "stringToDate")
  @Mapping(source = "value1", target = "value1")
  com.shs.b2bm.claim.service.entities.ServiceMonetary toEntity(
      com.shs.b2bm.claim.service.kafka.proto.ServiceMonetary serviceMonetary);
}
