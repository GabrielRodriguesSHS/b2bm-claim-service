package com.shs.b2bm.claim.service.dtos;

import java.math.BigDecimal;

/**
 * DTO containing details about part validation rules.
 *
 * @param required indicates if the part is required
 * @param minPriceValue minimum allowed price value for the part
 */
public record PartRulesDetailsDto (boolean required, BigDecimal minPriceValue) {}
