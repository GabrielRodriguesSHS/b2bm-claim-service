package com.shs.b2bm.claim.service.dtos;

import java.util.ArrayList;
import java.util.List;

public record ServiceOrderValidationResult(boolean isValid, List<String> errorsList) {}
