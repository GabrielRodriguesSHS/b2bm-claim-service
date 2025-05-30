package com.shs.b2bm.claim.service.dtos;

/**
 * DTO containing details about serial number validation rules.
 *
 * @param required indicates if the serial number is required
 * @param minLenght minimum allowed length for the serial number
 * @param maxLenght maximum allowed length for the serial number
 */
public record SerialNumberRulesDetailsDto(boolean required, Integer minLenght, Integer maxLenght) {}
