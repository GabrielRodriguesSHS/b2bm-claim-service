package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.JobCodes;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JobCodesProtoMapper}. Tests the mapping functionality between JobCodes
 * protobuf and entity objects.
 */
class JobCodesProtoMapperTest {

  private final JobCodesProtoMapper mapper = JobCodesProtoMapper.INSTANCE;

  /**
   * Tests that all fields from the JobCodes protobuf message are correctly mapped to the entity.
   */
  @Test
  void toEntity_ShouldMapAllFields() {
    // Arrange
    var protoBuilder =
        com.shs.b2bm.claim.service.kafka.proto.JobCodes.newBuilder()
            .setSequenceNumber(1)
            .setJobCode(123L)
            .setDescription("Test Job Code");
    var proto = protoBuilder.build();

    // Act
    JobCodes entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getSequenceNumber()).isEqualTo(1);
    assertThat(entity.getJobCode()).isEqualTo(123L);
    assertThat(entity.getDescription()).isEqualTo("Test Job Code");
  }

  /**
   * Tests that default values from the JobCodes protobuf message are correctly mapped to the
   * entity.
   */
  @Test
  void toEntity_WithDefaultValues_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.JobCodes.newBuilder();
    var proto = protoBuilder.build();

    // Act
    JobCodes entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getSequenceNumber()).isEqualTo(0); // Proto defaults to 0 for numbers
    assertThat(entity.getJobCode()).isEqualTo(0L); // Proto defaults to 0 for numbers
    assertThat(entity.getDescription()).isEqualTo(""); // Proto defaults to empty string
  }

  /**
   * Tests that null values from the JobCodes protobuf message are correctly mapped to the entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToNull() {
    // Act
    JobCodes entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
