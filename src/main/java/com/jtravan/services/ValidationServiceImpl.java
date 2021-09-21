package com.jtravan.services;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.jtravan.api.fhir.model.ValidationResponse;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionInput;
import com.jtravan.process.HapiFhirValidator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Coordinates the logic needed to facilitate a validation
 */
@Component
public class ValidationServiceImpl implements ValidationService {

    private final HapiFhirValidator hapiFhirValidator;
    private final ExecutionOutputParserService executionOutputParserService;
    private final ObjectMapper jacksonJsonObjectMapper;

    @Autowired
    public ValidationServiceImpl(@NonNull HapiFhirValidator hapiFhirValidator,
                                 @NonNull ExecutionOutputParserService executionOutputParserService,
                                 @NonNull ObjectMapper jacksonJsonObjectMapper) {
        this.hapiFhirValidator = hapiFhirValidator;
        this.executionOutputParserService = executionOutputParserService;
        this.jacksonJsonObjectMapper = jacksonJsonObjectMapper;
    }

    @Override
    public ValidationResponse validate(Object fhirJson, FhirVersionEnum version, Boolean isCodeSystemIgnored) throws IOException, UnhandledFhirVersionException {

        String jsonString = jacksonJsonObjectMapper.writeValueAsString(fhirJson == null ? "" : fhirJson);
        boolean isValidJsonString = isJSONValid(jsonString);

        if (isValidJsonString) {
            ExecutionInput executionInput = new Hl7FhirValidatorExecutionInput();
            executionInput.setJsonString(jsonString);
            executionInput.setVersion(version);
            executionInput.setIsCodeSystemIgnored(isCodeSystemIgnored);

            // Execute
            ExecutionOutput executionOutput = hapiFhirValidator.validate(executionInput);

            // Parse output
            return executionOutputParserService.parseValidationResult(executionOutput.getValidationResult());
        } else {
            throw new JsonSyntaxException("Could not process as Valid JSON");
        }
    }

    /**
     * Tests whether it's a valid JSON String
     * @param jsonInString the JSON string to test
     * @return true if it's valid, false otherwise
     */
    private boolean isJSONValid(String jsonInString ) {
        try {
            jacksonJsonObjectMapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
