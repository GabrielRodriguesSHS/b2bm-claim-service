package com.shs.b2bm.claim.service.enums;

public enum Rule {
    SerialNumberValidation("Serial Number Validation"),
    PartsValidation("Parts Validation");

    private final String description;

    Rule(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
