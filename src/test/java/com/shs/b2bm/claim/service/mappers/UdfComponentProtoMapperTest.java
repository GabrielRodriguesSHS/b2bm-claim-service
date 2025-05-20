package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.UdfComponent;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UdfComponentProtoMapper}. Tests the mapping functionality between
 * UdfComponent protobuf and entity objects.
 */
class UdfComponentProtoMapperTest {

  private final UdfComponentProtoMapper mapper = UdfComponentProtoMapper.INSTANCE;

  /**
   * Tests that all fields from the UdfComponent protobuf message are correctly mapped to the
   * entity.
   */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.UdfComponent.newBuilder()
            .setCode(999L)
            .setCharComponent("Test Component");
    var proto = protoBuilder.build();

    // Act
    UdfComponent entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCode()).isEqualTo(999L);
    assertThat(entity.getCharComponent()).isEqualTo("Test Component");
  }

  /**
   * Tests that default/null values from the UdfComponent protobuf message are correctly mapped to
   * the entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.UdfComponent.newBuilder();
    var proto = protoBuilder.build();

    // Act
    UdfComponent entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCode()).isEqualTo(0L); // Proto defaults to 0 for numbers
    assertThat(entity.getCharComponent()).isEqualTo(""); // Proto defaults to empty string
  }

  /** Tests that null values from the Parts protobuf message are correctly mapped to the entity. */
  @Test
  void toEntity_WithNullValues_ShouldMapToNull() {
    // Act
    UdfComponent entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
