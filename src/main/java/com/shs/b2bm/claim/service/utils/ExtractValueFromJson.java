package com.shs.b2bm.claim.service.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExtractValueFromJson {
    private final Map<String, Object> rules;

    public ExtractValueFromJson(Map<String, Object> rules) {
        this.rules = rules != null ? rules : new HashMap<>();
    }

    public String getStringRule(String key, String defaultValue) {
        Object value = rules.get(key);
        return value != null ? value.toString() : defaultValue;
    }

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
