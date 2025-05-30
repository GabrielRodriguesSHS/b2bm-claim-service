package com.shs.b2bm.claim.service.enums.converters;

import com.shs.b2bm.claim.service.enums.Rule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RuleConverter implements AttributeConverter<Rule, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Rule rule) {
        if (rule == null) {
            return null;
        }
        return rule.ordinal();
    }

    @Override
    public Rule convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Rule.values())
                .filter(r -> r.ordinal() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
