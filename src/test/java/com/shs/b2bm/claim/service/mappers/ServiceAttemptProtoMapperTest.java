package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.Timestamp;
import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ServiceAttemptProtoMapper}. Tests the mapping functionality between
 * ServiceAttempt protobuf and entity objects, including date and timestamp conversions.
 */
class ServiceAttemptProtoMapperTest {

  private final ServiceAttemptProtoMapper mapper = ServiceAttemptProtoMapper.INSTANCE;

  /**
   * Tests that all fields from the ServiceAttempt protobuf message are correctly mapped to the
   * entity, including proper conversion of dates and timestamps.
   */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    Instant now = Instant.now();
    Timestamp timestamp =
        Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();

    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.ServiceAttempt.newBuilder()
            .setCallDate("2024-03-15")
            .setTechnicianEmployeeNumber("ABC123")
            .setCallCode("CC001")
            .setStartTime(timestamp)
            .setEndTime(timestamp)
            .setTransitTimeInMinutes(30)
            .setTechnicianComment1("Comment 1")
            .setTechnicianComment2("Comment 2");
    var proto = protoBuilder.build();

    // Act
    ServiceAttempt entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCallDate()).isNotNull();
    assertThat(entity.getTechEmployeeNumber()).isEqualTo("ABC123");
    assertThat(entity.getCallCode()).isEqualTo("CC001");
    assertThat(entity.getStartTime()).isNotNull();
    assertThat(entity.getEndTime()).isNotNull();
    assertThat(entity.getTransitTimeInMinutes()).isEqualTo(30);
    assertThat(entity.getTechComment1()).isEqualTo("Comment 1");
    assertThat(entity.getTechComment2()).isEqualTo("Comment 2");
  }

  /**
   * Tests that default/null values from the ServiceAttempt protobuf message are correctly mapped to
   * the entity.
   */
  @Test
  void toEntity_WithDefaultValues_ShouldMapToDefault() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.ServiceAttempt.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceAttempt entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCallDate())
        .isNull(); // Custom date conversion returns null for empty string
    assertThat(entity.getTechEmployeeNumber()).isEqualTo("");
    assertThat(entity.getCallCode()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getStartTime()).isNull();
    assertThat(entity.getEndTime()).isNull();
    assertThat(entity.getTransitTimeInMinutes()).isEqualTo(0);
    assertThat(entity.getTechComment1()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getTechComment2()).isEqualTo(""); // Proto defaults to empty string
  }

  /**
   * Tests that invalid date format in the ServiceAttempt protobuf message results in an
   * IllegalArgumentException.
   */
  @Test
  void toEntity_WithInvalidDate_ShouldHandleError() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.ServiceAttempt.newBuilder()
            .setCallDate("invalid-date");
    var proto = protoBuilder.build();

    // Act & Assert
    org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class, () -> mapper.toEntity(proto));
  }

  /**
   * Tests that null values from the ServiceAttempt protobuf message are correctly mapped to the
   * entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToNull() {
    // Act
    ServiceAttempt entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
