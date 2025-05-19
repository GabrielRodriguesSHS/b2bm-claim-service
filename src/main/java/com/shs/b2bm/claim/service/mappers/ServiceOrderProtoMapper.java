package com.shs.b2bm.claim.service.mappers;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
      PartsProtoMapper.class,
      ServiceAttemptProtoMapper.class,
      JobCodesProtoMapper.class,
      ServiceMonetaryProtoMapper.class,
      UdfComponentProtoMapper.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceOrderProtoMapper {
  ServiceOrderProtoMapper INSTANCE = Mappers.getMapper(ServiceOrderProtoMapper.class);

  /*default ServiceOrder toProto(ClaimsDto.ServiceOrderDTO dto) {
    return ServiceOrder.newBuilder()
        .mergeFrom(mapMainFields(dto.serviceOrderData))
        .addAllParts(mapList(dto.parts, PartsProtoMapper.INSTANCE::toProto))
        .addAllServiceAttempts(
            mapList(dto.serviceAttempts, ServiceAttemptProtoMapper.INSTANCE::toProto))
        .addAllJobCodes(mapList(dto.jobCodes, JobCodesProtoMapper.INSTANCE::toProto))
        .addAllServiceMonetary(
            mapList(dto.serviceMonetary, ServiceMonetaryProtoMapper.INSTANCE::toProto))
        .addAllUdfComponent(mapList(dto.udfComponents, UdfComponentProtoMapper.INSTANCE::toProto))
        .build();
  }

  default ServiceOrder mapMainFields(String xml) {
    String[] fields = ExtractUtils.safeSplit(xml);
    return ServiceOrder.newBuilder()
        .setUnitNumber(ExtractUtils.extractField(fields, 0))
        .setServiceOrderNumber(ExtractUtils.extractField(fields, 1))
        .setClosedDate(ExtractUtils.extractField(fields, 2))
        .build();
  }*/

  static <T> List<T> mapList(List<String> source, Function<String, T> mapper) {
    if (source == null) {
      return Collections.emptyList();
    }
    return source.stream().map(mapper).collect(Collectors.toList());
  }
}
