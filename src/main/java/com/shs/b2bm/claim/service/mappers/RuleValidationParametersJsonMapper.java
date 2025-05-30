package com.shs.b2bm.claim.service.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.dtos.PartRulesDetailsDto;
import com.shs.b2bm.claim.service.dtos.SerialNumberRulesDetailsDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper interface for converting JSON parameter details from {@link RuleValidationConfig}
 * to their corresponding DTO representations.
 */
@Mapper
public interface RuleValidationParametersJsonMapper {
    /**
     * Singleton instance of the mapper.
     */
    RuleValidationParametersJsonMapper INSTANCE = Mappers.getMapper(RuleValidationParametersJsonMapper.class);

    /**
     * Converts the JSON parameter details from a {@link RuleValidationConfig} entity to a {@link PartRulesDetailsDto}.
     *
     * @param ruleValidationConfig the entity containing JSON parameter details
     * @return the mapped PartRulesDetailsDto, or null if conversion fails or parameters are absent
     */
    @Named("jsonToPartRulesDetailsDto")
    default PartRulesDetailsDto jsonToPartRulesDetailsDto(RuleValidationConfig ruleValidationConfig) {
        if (ruleValidationConfig.getParametersDetails() == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.readValue(ruleValidationConfig.getParametersDetails(), String.class);
            return mapper.readValue(json, PartRulesDetailsDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts the JSON parameter details from a {@link RuleValidationConfig} entity to a {@link SerialNumberRulesDetailsDto}.
     *
     * @param ruleValidationConfig the entity containing JSON parameter details
     * @return the mapped SerialNumberRulesDetailsDto, or null if conversion fails or parameters are absent
     */
    @Named("jsonToSerialNumberRulesDetailsDto")
    default SerialNumberRulesDetailsDto jsonToSerialNumberRulesDetailsDto(RuleValidationConfig ruleValidationConfig) {
        if (ruleValidationConfig.getParametersDetails() == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.readValue(ruleValidationConfig.getParametersDetails(), String.class);
            return mapper.readValue(json, SerialNumberRulesDetailsDto.class);
        } catch (Exception e) {
            return null;
        }
    }

}
