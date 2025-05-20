package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shs.b2bm.claim.service.entities.ServiceMonetary;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ServiceMonetaryProtoMapper}. Tests the mapping functionality between
 * ServiceMonetary protobuf and entity objects, including date and numeric value conversions.
 */
class ServiceMonetaryProtoMapperTest {

  private final ServiceMonetaryProtoMapper mapper = ServiceMonetaryProtoMapper.INSTANCE;

  /**
   * Tests that all fields from the ServiceMonetary protobuf message are correctly mapped to the
   * entity, including proper conversion of dates and numeric values.
   */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.ServiceMonetary.newBuilder()
            .setTypePayment("CREDIT")
            .setDatePayment("2024-03-15")
            .setValue1(100.50);
    var proto = protoBuilder.build();

    // Act
    ServiceMonetary entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getTypePayment()).isEqualTo("CREDIT");
    assertThat(entity.getDatePayment()).isNotNull();
    assertThat(entity.getValue1()).isEqualTo(100.50);
  }

  /**
   * Tests that default/null values from the ServiceMonetary protobuf message are correctly mapped
   * to the entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.ServiceMonetary.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceMonetary entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getTypePayment()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getDatePayment())
        .isNull(); // Custom date conversion returns null for empty string
    assertThat(entity.getValue1()).isEqualTo(0.0); // Proto defaults to 0 for numbers
  }

  /**
   * Tests that invalid date format in the ServiceMonetary protobuf message results in an
   * IllegalArgumentException.
   */
  @Test
  void toEntity_WithInvalidDate_ShouldThrowException() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.ServiceMonetary.newBuilder()
            .setDatePayment("invalid-date");
    var proto = protoBuilder.build();

    // Act & Assert
    assertThatThrownBy(() -> mapper.toEntity(proto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid date format");
  }

  /**
   * Tests that null values from the ServiceAttempt protobuf message are correctly mapped to the
   * entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToNull() {
    // Act
    ServiceMonetary entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
