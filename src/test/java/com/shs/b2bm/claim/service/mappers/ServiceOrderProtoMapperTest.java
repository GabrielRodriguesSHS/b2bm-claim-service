package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import java.util.Arrays;
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
        com.shs.b2bm.claim.service.kafka.proto.ServiceOrder.newBuilder()
            .setUnitNumber("UNIT001")
            .setServiceOrderNumber("SO001")
            .setClosedDate("2024-03-15")
            .addAllServiceAttempts(
                Arrays.asList(
                    com.shs.b2bm.claim.service.kafka.proto.ServiceAttempt.newBuilder()
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
    assertThat(entity.getServiceOrderNumber()).isEqualTo("SO001");
    assertThat(entity.getClosedDate()).isNotNull();

    // Verify Service Attempts
    assertThat(entity.getServiceAttempts()).hasSize(1);
    assertThat(entity.getServiceAttempts().get(0).getTechEmployeeNumber()).isEqualTo("ABC123");
    assertThat(entity.getServiceAttempts().get(0).getCallCode()).isEqualTo("CC001");
  }

  /**
   * Tests that default/null values from the ServiceOrder protobuf message are correctly mapped to
   * the entity.
   */
  @Test
  void toEntity_WithNullValues_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.ServiceOrder.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(proto);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getUnitNumber()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getServiceOrderNumber()).isEqualTo(""); // Proto defaults to empty string
    assertThat(entity.getClosedDate())
        .isNull(); // Custom date conversion returns null for empty string
    assertThat(entity.getServiceAttempts()).isEmpty();
  }

  /** Tests that mapping a null list returns an empty list. */
  @Test
  void mapList_WithNullList_ShouldReturnEmptyList() {
    assertThat(ServiceOrderProtoMapper.mapList(null, String::toUpperCase)).isEmpty();
  }

  /** Tests that mapping a valid list applies the mapping function to all elements. */
  @Test
  void mapList_WithValidList_ShouldMapAllElements() {
    var result =
        ServiceOrderProtoMapper.mapList(Arrays.asList("test1", "test2"), String::toUpperCase);

    assertThat(result).hasSize(2).containsExactly("TEST1", "TEST2");
  }

  /** Tests that mapping a null proto returns null. */
  @Test
  void toEntity_WithNullProto_ShouldMapToDefaults() {
    // Arrange
    var protoBuilder = com.shs.b2bm.claim.service.kafka.proto.ServiceOrder.newBuilder();
    var proto = protoBuilder.build();

    // Act
    ServiceOrder entity = mapper.toEntity(null);

    // Assert
    assertThat(entity).isNull();
  }
}
