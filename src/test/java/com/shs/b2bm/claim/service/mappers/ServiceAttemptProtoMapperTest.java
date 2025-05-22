package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.Timestamp;
import com.shs.b2bm.claim.service.entities.ServiceAttempt;
import com.shs.b2bm.claim.service.kafka.proto.ServiceAttemptProto;
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
        ServiceAttemptProto.newBuilder()
            .setCallDate("2024-03-15")
            .setTechnicianEmployeeNumber("ABC123")
            .setCallCode("CC001")
            .setStartTime(timestamp.toString())
            .setEndTime(timestamp.toString())
            .setTransitTimeInMinutes("30")
            .setTechnicianComment1("Comment 1")
            .setTechnicianComment2("Comment 2");
    var proto = protoBuilder.build();

    // Act
    ServiceAttempt entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCallDate()).isNotNull();
    assertThat(entity.getTechnicianEmployeeNumber()).isEqualTo("ABC123");
    assertThat(entity.getCallCode()).isEqualTo("CC001");
    assertThat(entity.getStartTime()).isNotNull();
    assertThat(entity.getEndTime()).isNotNull();
    assertThat(entity.getTransitTimeInMinutes()).isEqualTo("30");
    assertThat(entity.getTechnicianComment1()).isEqualTo("Comment 1");
    assertThat(entity.getTechnicianComment2()).isEqualTo("Comment 2");
  }

  /**
   * Tests that default/null values from the ServiceAttempt protobuf message are correctly mapped to
   * the entity.
   */
  @Test
  void toEntity_WithDefaultValues_ShouldMapToDefault() {
    // Arrange
    var protoBuilder = ServiceAttemptProto.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceAttempt entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getCallDate()).isEqualTo("");
    assertThat(entity.getTechnicianEmployeeNumber()).isEqualTo("");
    assertThat(entity.getCallCode()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getStartTime()).isEqualTo("");
    assertThat(entity.getEndTime()).isEqualTo("");
    assertThat(entity.getTransitTimeInMinutes()).isEqualTo("");
    assertThat(entity.getTechnicianComment1()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getTechnicianComment2()).isEqualTo(""); // Proto defaults to empty string
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
