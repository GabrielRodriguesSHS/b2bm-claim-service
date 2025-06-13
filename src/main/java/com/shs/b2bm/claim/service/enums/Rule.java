package com.shs.b2bm.claim.service.enums;

/**
 * Enum representing different types of validation rules. Each rule has a description for display or
 * documentation purposes.
 */
public enum Rule {
    SERIAL_NUMBER_VALIDATION("SerialNumberValidation"),
    PARTS_VALIDATION("PartsValidation"),
    APPROVED_BRAND("ApprovedBrand"),
    APPROVED_MODEL_NUMBER("ApprovedModelNumber"),
    MANUAL_REVIEW_PROC_ID("ManualReviewProcId"),
    KEYWORDS_MANUAL_ENTRIES("KeywordsManualEntries");

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
