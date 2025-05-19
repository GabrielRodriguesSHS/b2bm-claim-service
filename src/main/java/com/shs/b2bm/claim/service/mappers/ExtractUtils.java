package com.shs.b2bm.claim.service.mappers;

public class ExtractUtils {

  /**
   * Safely splits a pipe-delimited string, returning an empty array if the input is null or empty.
   *
   * @param value the string to split
   * @return the split array, or empty array if input is null/empty
   */
  public static String[] safeSplit(String value) {
    if (value == null || value.isEmpty()) {
      return new String[0];
    }
    return value.split("\\|", -1);
  }

  public static String extractField(String[] fields, int index) {
    return fields.length > index ? fields[index].trim() : "";
  }

  public static int extractIntField(String[] fields, int index) {
    String field = extractField(fields, index);
    try {
      return Integer.parseInt(field);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static long extractLongField(String[] fields, int index) {
    String field = extractField(fields, index);
    try {
      return Long.parseLong(field);
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  public static double extractDoubleField(String[] fields, int index) {
    String field = extractField(fields, index);
    try {
      return Double.parseDouble(field);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }
}
