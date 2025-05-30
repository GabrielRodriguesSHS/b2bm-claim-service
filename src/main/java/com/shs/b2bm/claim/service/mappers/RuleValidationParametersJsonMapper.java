package com.shs.b2bm.claim.service.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shs.b2bm.claim.service.dtos.PartRulesDetailsDto;
import com.shs.b2bm.claim.service.dtos.SerialNumberRulesDetailsDto;
import com.shs.b2bm.claim.service.entities.RuleValidationConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RuleValidationParametersJsonMapper {
    RuleValidationParametersJsonMapper INSTANCE = Mappers.getMapper(RuleValidationParametersJsonMapper.class);

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
