package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.Parts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PartsProtoMapper}. Tests the mapping functionality between Parts protobuf
 * and entity objects.
 */
class PartsProtoMapperTest {

  private final PartsProtoMapper mapper = PartsProtoMapper.INSTANCE;

  /** Tests that all fields from the Parts protobuf message are correctly mapped to the entity. */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.Parts.newBuilder()
            .setSequenceNumber(1)
            .setPartCode("P123")
            .setPartDescription("Test Part");
    var proto = protoBuilder.build();

    // Act
    Parts entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getSequenceNumber()).isEqualTo(1);
    assertThat(entity.getPartCode()).isEqualTo("P123");
    assertThat(entity.getPartDescription()).isEqualTo("Test Part");
  }

  /**
   * Tests that default values from the Parts protobuf message are correctly mapped to the entity.
   */
  @Test
  void toEntity_WithDefaultValues_ShouldMapToDefault() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.Parts.newBuilder();
    var proto = protoBuilder.build();

    // Act
    Parts entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getSequenceNumber()).isEqualTo(0); // Proto defaults to 0 for numbers
    assertThat(entity.getPartCode()).isEmpty(); // Proto defaults to empty string
    assertThat(entity.getPartDescription()).isEmpty(); // Proto defaults to empty string
  }

  /** Tests that null values from the Parts protobuf message are correctly mapped to the entity. */
  @Test
  void toEntity_WithNullValues_ShouldMapToNull() {
    // Act
    Parts entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
