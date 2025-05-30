package com.shs.b2bm.claim.service.dtos;

import java.math.BigDecimal;

public record SerialNumberRulesDetailsDto(boolean required, Integer minLenght, Integer maxLenght) {}
