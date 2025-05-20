package com.shs.b2bm.claim.service.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link ExtractUtils}. Tests date and timestamp conversion utilities used by the
 * mappers.
 */
class ExtractUtilsTest {

  private final ExtractUtils extractUtils = new ExtractUtils();

  /**
   * Tests that a valid date string in yyyy-MM-dd format is correctly converted to a Date object.
   */
  @Test
  void stringToDate_WithValidDate_ShouldReturnDate() {
    // Arrange
    String dateStr = "2024-03-15";

    // Act
    LocalDate result = ExtractUtils.stringToDate(dateStr);

    // Assert
    assertThat(result.getYear()).isEqualTo(2024);
    assertThat(result.getMonthValue()).isEqualTo(3);
    assertThat(result.getDayOfMonth()).isEqualTo(15);
  }

  /**
   * Tests that invalid date formats throw an IllegalArgumentException.
   *
   * @param input The invalid date string to test
   */
  @ParameterizedTest
  @ValueSource(strings = {"invalid-date", "2024/03/15", "15-03-2024"})
  void stringToDate_WithInvalidInput_ShouldThrowException(String input) {
    assertThatThrownBy(() -> ExtractUtils.stringToDate(input))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid date format");
  }

  /** Tests that null input returns null output for date conversion. */
  @Test
  void stringToDate_WithNull_ShouldReturnNull() {
    assertThat(ExtractUtils.stringToDate(null)).isNull();
  }

  /** Tests that a valid Protobuf Timestamp is correctly converted to LocalDateTime. */
  @Test
  void timestampToLocalDateTime_WithValidTimestamp_ShouldConvert() {
    // Arrange
    Instant now = Instant.now();
    Timestamp timestamp =
        Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();

    // Act
    LocalDateTime result = extractUtils.timestampToLocalDateTime(timestamp);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.atZone(ZoneId.systemDefault()).toInstant())
        .isCloseTo(now, within(1, ChronoUnit.MILLIS));
  }

  /** Tests that null input returns null output for timestamp conversion. */
  @Test
  void timestampToLocalDateTime_WithNull_ShouldReturnNull() {
    assertThat(extractUtils.timestampToLocalDateTime(null)).isNull();
  }
}
