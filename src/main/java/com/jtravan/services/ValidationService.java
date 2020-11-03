package com.jtravan.services;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.jtravan.api.fhir.model.ValidationResponse;
import com.jtravan.exceptions.UnhandledFhirVersionException;

import java.io.IOException;

public interface ValidationService {

    ValidationResponse validate(Object fhirJson, FhirVersionEnum version) throws IOException, UnhandledFhirVersionException;
}
