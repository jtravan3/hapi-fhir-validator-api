package com.jtravan.model;

import ca.uhn.fhir.context.FhirVersionEnum;
import lombok.Data;

import java.io.File;

/**
 * Abstract POJO that provides a simple model of the input of a given
 * validation execution
 */
@Data
public abstract class ExecutionInput {
    private String jsonString;
    private File json;
    private FhirVersionEnum version;
    private Boolean isCodeSystemIgnored;
}
