package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.kafka.proto.ServiceAttemptProto;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import java.util.List;
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
            .setUnitNumber("UNIT001")
            .setOrderNumber("SO001")
            .setClosedDate("2024-03-15")
            .addAllServiceAttempts(
                List.of(
                    ServiceAttemptProto.newBuilder()
                        .setCallDate("2024-03-15")
                        .setTechnicianEmployeeNumber("ABC123")
                        .setCallCode("CC001")
                        .build()));
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getUnitNumber()).isEqualTo("UNIT001");
    assertThat(entity.getOrderNumber()).isEqualTo("SO001");
    assertThat(entity.getClosedDate()).isNotNull();

    // Verify Service Attempts
    assertThat(entity.getServiceAttemptsList()).hasSize(1);
    assertThat(entity.getServiceAttemptsList().getFirst().getTechnicianEmployeeNumber())
        .isEqualTo("ABC123");
    assertThat(entity.getServiceAttemptsList().getFirst().getCallCode()).isEqualTo("CC001");
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
    assertThat(entity.getUnitNumber()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getOrderNumber()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getClosedDate())
        .isEqualTo(""); // Custom date conversion returns null for empty string
    assertThat(entity.getServiceAttemptsList()).isEmpty();
  }

  /** Tests that mapping a null proto returns null. */
  @Test
  void toEntity_WithNullProto_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = ServiceOrderProto.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
