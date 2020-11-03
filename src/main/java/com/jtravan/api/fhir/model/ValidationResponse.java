package com.jtravan.api.fhir.model;

import ca.uhn.fhir.validation.SingleValidationMessage;
import lombok.Data;

import java.util.List;

/**
 * Simple POJO that includes the output of an execution
 */
@Data
public class ValidationResponse {
    private Boolean successful;
    private List<SingleValidationMessage> errors;
    private List<SingleValidationMessage> warnings;
    private List<SingleValidationMessage> info;
    private String exception;
}
