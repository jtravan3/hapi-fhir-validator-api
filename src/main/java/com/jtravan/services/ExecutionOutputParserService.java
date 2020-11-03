package com.jtravan.services;

import ca.uhn.fhir.validation.ValidationResult;
import com.jtravan.api.fhir.model.ValidationResponse;

public interface ExecutionOutputParserService {

    ValidationResponse parseValidationResult(ValidationResult validationResult);
}
