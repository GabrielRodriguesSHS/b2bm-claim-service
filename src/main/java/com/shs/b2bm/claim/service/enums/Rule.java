package com.shs.b2bm.claim.service.enums;

/**
 * Enum representing different types of validation rules. Each rule has a description for display or
 * documentation purposes.
 */
public enum Rule {
  /** Rule for validating serial numbers. */
  SerialNumberValidation("Serial Number Validation"),
  /** Rule for validating parts. */
  PartsValidation("Parts Validation");

  private final String description;

  /**
   * Constructs a Rule enum with the specified description.
   *
   * @param description the description of the rule
   */
  Rule(String description) {
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
