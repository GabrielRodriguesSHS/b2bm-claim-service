package com.shs.b2bm.claim.service.dtos;

import java.math.BigDecimal;

public record PartRulesDetailsDto (boolean required, BigDecimal minPriceValue) {}
