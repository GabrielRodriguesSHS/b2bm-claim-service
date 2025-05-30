# Service Order Rule Creation Guide

> **Purpose:**
> This guide explains how to add or modify validation rules for Service Orders, enabling easy integration and maintenance of business logic for claim approval.

---

## Overview

When processing a new Service Order, a set of rules is validated to determine if it can be transformed into a Claim and approved automatically. If any rule fails, the order requires manual review. This guide outlines the steps to create, configure, and integrate new rules efficiently.

---

## Architecture Context

- **Validation Entry Point:** `ServiceOrderValidationServiceImpl`
- **Validation Method:** `validateMessage`
- **Rule Integration:** All beans implementing the `ServiceOrderRuleValidatorService` interface are auto-discovered and executed.

> **To add a new rule, simply implement the `ServiceOrderRuleValidatorService` interface.**

---

## Steps to Create a New Rule

### 1. Register the Rule in the Enum
- Add your rule to the `Rule` enum.
- **Rule ID:** The position in the enum (starting from 1).

```java
public enum Rule {
    EXISTING_RULE_1,
    EXISTING_RULE_2,
    YOUR_NEW_RULE // Position = Rule ID
}
```

### 2. Configure the Rule in the Database
Insert a new row in the `rule_validation_config` table:

| Field              | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| `rule_id`          | Position of your rule in the `Rule` enum (starting from 1)                  |
| `partner_id`       | `NULL` for all clients, or specific value for client-specific rules          |
| `parameters_details` | JSON object with all necessary parameters for the rule                      |
| `error_message`    | Message shown to the user if the rule fails                                 |

> **Note:**
> If your rule should apply to multiple specific partners, you must create a separate entry in the table for each `partner_id` with the same `rule_id` and appropriate parameters.

**Example:**
```json
{
  "rule_id": 3,
  "partner_id": null,
  "parameters_details": { "minAmount": 100 },
  "error_message": "Order amount must be at least 100."
}
```

**Example for specific partners:**
```json
{
  "rule_id": 3,
  "partner_id": 101,
  "parameters_details": { "minAmount": 200 },
  "error_message": "Order amount must be at least 200 for Partner 101."
}
{
  "rule_id": 3,
  "partner_id": 102,
  "parameters_details": { "minAmount": 150 },
  "error_message": "Order amount must be at least 150 for Partner 102."
}
```

### 3. Create a Parameters DTO
- Define a Java `record` to represent the `parameters_details` JSON.
- Fields must match the JSON structure.

```java
/**
 * DTO for minimum amount rule parameters.
 */
public record MinAmountRuleParameters(@NotNull Integer minAmount) {}
```

### 4. Add a JSON Mapper
- Update `RuleValidationParametersJsonMapper` to map the JSON to your new DTO.

### 5. Implement the Rule Validator
- Implement the `ServiceOrderRuleValidatorService` interface.
- **Naming convention:** `<RULE_NAME>ServiceOrderValidatorServiceImpl`

```java
@Service
public class MinAmountServiceOrderValidatorServiceImpl implements ServiceOrderRuleValidatorService {
    // Implementation here
}
```

---

## Naming Pattern

> **Rule Implementation:** `<RULE_NAME>ServiceOrderValidatorServiceImpl`

---

## Example Workflow

1. Add `MIN_AMOUNT` to `Rule` enum (ID = 3).
2. Insert config in `rule_validation_config` with `rule_id = 3`.
3. Create `MinAmountRuleParameters` record.
4. Map JSON in `RuleValidationParametersJsonMapper`.
5. Implement `MinAmountServiceOrderValidatorServiceImpl`.

---

## Notes & Best Practices

- **Keep rules atomic:** Each rule should validate a single business condition.
- **Use meaningful error messages:** Help users understand why validation failed.
- **Document your DTOs and services:** Use JavaDoc for clarity and maintainability.
- **Follow naming conventions:** Ensures consistency and discoverability.

---

## Need Help?
For questions or support, contact the platform maintainers or refer to the codebase documentation.

 