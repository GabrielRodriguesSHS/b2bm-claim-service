package com.shs.b2bm.claim.service.mappers;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for converting between ServiceOrder proto and entity objects. Uses various
 * component mappers for handling nested objects and custom type conversions.
 */
@Mapper
public interface ServiceOrderProtoMapper {
    ServiceOrderProtoMapper INSTANCE = Mappers.getMapper(ServiceOrderProtoMapper.class);

    ServiceOrder toEntity(ServiceOrderProto serviceOrder);
}
