package com.jtravan.api.fhir.v5;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.jtravan.api.fhir.model.ValidationResponse;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.FhirMediaType;
import com.jtravan.services.ValidationService;
import io.swagger.annotations.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Rest endpoints for the FHIR release 5.0 validation
 */
@RestController
@RequestMapping(
        value = "/fhir/r5",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = {FhirMediaType.APPLICATION_JSON_VALUE, FhirMediaType.APPLICATION_FHIR_JSON_VALUE})
@Api(tags = {"FHIR r5 (or 4.4.0)"})
public class ValidationEndpointV5 {

    private final ValidationService validationService;

    @Autowired
    public ValidationEndpointV5(@NonNull ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(value = "/validate")
    @ApiOperation(value = "Validates a FHIR object against the 4.4.0 schemas", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource was not found"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 422, message = "Cannot process FHIR JSON")
    }
    )
    public ValidationResponse validate(@ApiParam(name = "fhirJson", value = "FHIR JSON object to be validated", required = true)
                                       @RequestBody Object fhirJson,
                                       @RequestHeader(value = "X-Validate-Ignore-Codesystem", required = false) Boolean isCodeSystemsIgnored) throws IOException, UnhandledFhirVersionException {
        return validationService.validate(fhirJson, FhirVersionEnum.R5, isCodeSystemsIgnored);
    }

}
