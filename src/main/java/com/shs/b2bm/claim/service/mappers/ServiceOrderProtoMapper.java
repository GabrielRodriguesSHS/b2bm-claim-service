package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for converting between ServiceOrder proto and entity objects. Uses various
 * component mappers for handling nested objects and custom type conversions.
 */
@Mapper(
    uses = {ServiceAttemptProtoMapper.class, ExtractUtils.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceOrderProtoMapper {
  ServiceOrderProtoMapper INSTANCE = Mappers.getMapper(ServiceOrderProtoMapper.class);

  static <T> List<T> mapList(List<String> source, Function<String, T> mapper) {
    if (source == null) {
      return Collections.emptyList();
    }
    return source.stream().map(mapper).collect(Collectors.toList());
  }

  @Mapping(source = "closedDate", target = "closedDate", qualifiedByName = "stringToDate")
  @Mapping(source = "serviceAttemptsList", target = "serviceAttempts")
  ServiceOrder toEntity(com.shs.b2bm.claim.service.kafka.proto.ServiceOrder serviceOrder);
}
