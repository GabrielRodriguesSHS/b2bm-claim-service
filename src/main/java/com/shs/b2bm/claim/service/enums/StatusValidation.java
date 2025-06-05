package com.shs.b2bm.claim.service.enums;

/**
 * Enum representing different types of validation status. Each rule has a description for display or
 * documentation purposes.
 */
public enum StatusValidation {
  Error("error"),
  Success("success");

  private final String description;

  /**
   * Constructs a Rule enum with the specified description.
   *
   * @param description the description of the rule
   */
  StatusValidation(String description) {
    this.description = description;
  }

  /**
   * Gets the description of the rule.
   *
   * @return the rule description
   */
  public String getDescription() {
    return description;
  }
}
