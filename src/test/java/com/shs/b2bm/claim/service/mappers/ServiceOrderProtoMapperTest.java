package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ServiceOrderProtoMapper}. Tests the mapping functionality between protobuf
 * and entity objects.
 */
class ServiceOrderProtoMapperTest {

  private final ServiceOrderProtoMapper mapper = ServiceOrderProtoMapper.INSTANCE;

  /**
   * Tests that all fields from the ServiceOrder protobuf message are correctly mapped to the
   * entity, including nested collections.
   */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    var protoBuilder =
        ServiceOrderProto.newBuilder()
            .setServiceUnitNumber("UNIT001")
            .setServiceOrderNumber("SO001");
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getServiceUnitNumber()).isEqualTo("UNIT001");
    assertThat(entity.getServiceOrderNumber()).isEqualTo("SO001");
  }

  /**
   * Tests that default/null values from the ServiceOrder protobuf message are correctly mapped to
   * the entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = ServiceOrderProto.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getServiceUnitNumber()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getServiceOrderNumber()).isEqualTo(""); // Proto defaults to empty string
  }

  /** Tests that mapping a null proto returns null. */
  @Test
  void toEntity_WithNullProto_ShouldMapToDefaults() {
    // Act
    ServiceOrder entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
