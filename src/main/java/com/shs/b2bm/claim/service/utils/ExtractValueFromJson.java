package com.shs.b2bm.claim.service.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for extracting typed values from JSON-like Map structures.
 * Provides safe type conversion methods with default value fallbacks for common data types.
 * Handles type coercion from various object types including String representations.
 */
public class ExtractValueFromJson {
  private final Map<String, Object> rules;

  /**
   * Constructs a new ExtractValueFromJson instance with the provided rules map.
   * If the provided map is null, an empty HashMap will be used instead.
   *
   * @param rules the map containing key-value pairs to extract values from, may be null
   */
  public ExtractValueFromJson(Map<String, Object> rules) {
    this.rules = rules != null ? rules : new HashMap<>();
  }

  /**
   * Extracts a string value from the rules map for the specified key.
   * Converts the value to string using toString() if present, otherwise returns the default value.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found or value is null
   * @return the string value associated with the key, or defaultValue if not found or null
   * @throws NullPointerException if key is null
   */
  public String getStringRule(String key, String defaultValue) {
    Object value = rules.get(key);
    return value != null ? value.toString() : defaultValue;
  }

  /**
   * Extracts an integer value from the rules map for the specified key.
   * Attempts to convert Number objects directly or parse String values as integers.
   * Returns the default value if conversion fails or key is not found.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or conversion fails
   * @return the integer value associated with the key, or defaultValue if not found or conversion fails
   * @throws NullPointerException if key is null
   */
  public int getIntRule(String key, int defaultValue) {
    Object value = rules.get(key);
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    if (value instanceof String) {
      try {
        return Integer.parseInt((String) value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    return defaultValue;
  }

  /**
   * Extracts a boolean value from the rules map for the specified key.
   * Handles Boolean objects directly or parses String values using Boolean.parseBoolean().
   * Returns the default value if key is not found or value cannot be converted.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or conversion fails
   * @return the boolean value associated with the key, or defaultValue if not found or conversion fails
   * @throws NullPointerException if key is null
   */
  public boolean getBooleanRule(String key, boolean defaultValue) {
    Object value = rules.get(key);
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    if (value instanceof String) {
      return Boolean.parseBoolean((String) value);
    }
    return defaultValue;
  }

  /**
   * Extracts a BigDecimal value from the rules map for the specified key.
   * Converts Number objects to BigDecimal or parses String values as BigDecimal.
   * Returns the default value if conversion fails or key is not found.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or conversion fails
   * @return the BigDecimal value associated with the key, or defaultValue if not found or conversion fails
   * @throws NullPointerException if key is null
   */
  public BigDecimal getDecimalRule(String key, BigDecimal defaultValue) {
    Object value = rules.get(key);
    if (value instanceof Number) {
      return BigDecimal.valueOf(((Number) value).doubleValue());
    }
    if (value instanceof String) {
      try {
        return new BigDecimal((String) value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    return defaultValue;
  }
}
