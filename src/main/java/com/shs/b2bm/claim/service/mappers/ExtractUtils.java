package com.shs.b2bm.claim.service.mappers;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.mapstruct.Named;

public class ExtractUtils {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Converts a string date in yyyy-MM-dd format to a Date object using thread-safe Java Time API.
   *
   * @param dateStr the date string to parse
   * @return the parsed Date object, or null if the input is null or empty
   * @throws IllegalArgumentException if the date string is not in the expected format
   */
  @Named("stringToDate")
  public static LocalDate stringToDate(String dateStr) {
    if (dateStr == null || dateStr.trim().isEmpty()) {
      return null;
    }
    try {
      return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
    }
  }

  /**
   * Converts a Protobuf Timestamp to LocalDateTime.
   *
   * @param timestamp the Protobuf Timestamp to convert
   * @return the converted LocalDateTime, or null if input is null
   */
  @Named("timestampToLocalDateTime")
  public LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
        ZoneId.systemDefault());
  }
}
