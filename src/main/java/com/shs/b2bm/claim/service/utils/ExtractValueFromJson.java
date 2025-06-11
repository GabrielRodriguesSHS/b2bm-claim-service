package com.shs.b2bm.claim.service.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for extracting typed values from JSON-like Map structures. Provides safe type
 * conversion methods with default value fallbacks for common data types. Handles type coercion from
 * various object types including String representations.
 */
@Slf4j
@NoArgsConstructor
@Component
public class ExtractValueFromJson {

  /**
   * Extracts a string value from the rules map for the specified key. Converts the value to string
   * using toString() if present, otherwise returns the default value.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found or value is null
   * @return the string value associated with the key, or defaultValue if not found or null
   * @throws NullPointerException if key is null
   */
  public String getString(JsonNode jsonNode, String key, String defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    return (jsonNode != null && jsonNode.get(key) != null)
        ? jsonNode.get(key).asText()
        : defaultValue;
  }

  /**
   * Extracts an integer value from the rules map for the specified key. Attempts to convert Number
   * objects directly or parse String values as integers. Returns the default value if conversion
   * fails or key is not found.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or
   *     conversion fails
   * @return the integer value associated with the key, or defaultValue if not found or conversion
   *     fails
   * @throws NullPointerException if key is null
   */
  public int getInt(JsonNode jsonNode, String key, int defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    return (jsonNode != null && jsonNode.get(key) != null)
        ? jsonNode.get(key).asInt()
        : defaultValue;
  }

  /**
   * Extracts a boolean value from the rules map for the specified key. Handles Boolean objects
   * directly or parses String values using Boolean.parseBoolean(). Returns the default value if key
   * is not found or value cannot be converted.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or
   *     conversion fails
   * @return the boolean value associated with the key, or defaultValue if not found or conversion
   *     fails
   * @throws NullPointerException if key is null
   */
  public boolean getBoolean(JsonNode jsonNode, String key, boolean defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    return (jsonNode != null && jsonNode.get(key) != null)
        ? jsonNode.get(key).asBoolean()
        : defaultValue;
  }

  /**
   * Extracts a Double value from the rules map for the specified key. Converts Number objects to
   * Double or parses String values as Double. Returns the default value if conversion fails or key
   * is not found.
   *
   * @param key the key to look up in the rules map, must not be null
   * @param defaultValue the default value to return if key is not found, value is null, or
   *     conversion fails
   * @return the Double value associated with the key, or defaultValue if not found or conversion
   *     fails
   * @throws NullPointerException if key is null
   */
  public double getDouble(JsonNode jsonNode, String key, double defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    return (jsonNode != null && jsonNode.get(key) != null)
        ? jsonNode.get(key).asDouble()
        : defaultValue;
  }

  public List<String> getListOfString(JsonNode jsonNode, String key, List<String> defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    if (jsonNode != null && jsonNode.has(key) && jsonNode.get(key).isArray()) {
      return StreamSupport.stream(jsonNode.get(key).spliterator(), false)
          .filter(JsonNode::isTextual)
          .map(JsonNode::asText)
          .toList();
    }

    return defaultValue;
  }

  public List<Integer> getListOfInteger(JsonNode jsonNode, String key, List<Integer> defaultValue) {
    jsonNode = this.validateJsonNode(jsonNode);

    if (jsonNode != null && jsonNode.has(key) && jsonNode.get(key).isArray()) {
      return StreamSupport.stream(jsonNode.get(key).spliterator(), false)
          .filter(JsonNode::isInt)
          .map(JsonNode::asInt)
          .toList();
    }

    return defaultValue;
  }

  private JsonNode validateJsonNode(JsonNode jsonNode) {
    if (jsonNode != null && jsonNode.isTextual()) {
      ObjectMapper mapper = new ObjectMapper();
      try {
        jsonNode = mapper.readTree(jsonNode.asText()); // agora vira ObjectNode
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }

    return jsonNode;
  }
}
