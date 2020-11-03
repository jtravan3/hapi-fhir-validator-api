package com.jtravan.model;

import ca.uhn.fhir.validation.ValidationResult;
import lombok.Data;

/**
 * Abstract POJO that provides a simple model of the output of a given
 * validation execution
 */
@Data
public abstract class ExecutionOutput {
    private String standardOutput;
    private String standardError;
    private ValidationResult validationResult;
}
