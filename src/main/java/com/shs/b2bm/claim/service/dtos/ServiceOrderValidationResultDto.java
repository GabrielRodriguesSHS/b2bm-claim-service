package com.shs.b2bm.claim.service.dtos;

import java.util.List;

/**
 * DTO representing the result of a service order validation.
 *
 * @param isValid indicates if the service order is valid
 * @param errorsList list of validation error messages, if any
 */
public record ServiceOrderValidationResultDto(boolean isValid, List<String> errorsList) {}
