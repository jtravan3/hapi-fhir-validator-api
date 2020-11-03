package com.jtravan.exceptions;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.gson.JsonSyntaxException;
import com.jtravan.api.fhir.model.ValidationResponse;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ValidationEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { DataFormatException.class })
    public ResponseEntity<ValidationResponse> handleDataFormatException(DataFormatException ex) {
        ValidationResponse validationResponse = formatValidationResponse(ex);
        return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { FHIRFormatError.class })
    public ResponseEntity<ValidationResponse> handleFhirFormatError(FHIRFormatError ex) {
        ValidationResponse validationResponse = formatValidationResponse(ex);
        return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { InvalidRequestException.class })
    public ResponseEntity<ValidationResponse> handleInvalidRequestException(InvalidRequestException ex) {
        ValidationResponse validationResponse = formatValidationResponse(ex);
        return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<ValidationResponse> handleIllegalArgumenttException(IllegalArgumentException ex) {
        ValidationResponse validationResponse = formatValidationResponse(ex);
        return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { JsonSyntaxException.class })
    public ResponseEntity<ValidationResponse> handleJsonSyntaxException(JsonSyntaxException ex) {
        ValidationResponse validationResponse = formatValidationResponse(ex);
        return new ResponseEntity<>(validationResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ValidationResponse formatValidationResponse(Exception ex) {
        if ("Unexpected failure while validating resource".equals(ex.getMessage())) {
            ValidationResponse validationResponse = new ValidationResponse();
            validationResponse.setSuccessful(false);
            validationResponse.setException(ex.getCause().getMessage());
            return validationResponse;
        } else {
            ValidationResponse validationResponse = new ValidationResponse();
            validationResponse.setSuccessful(false);
            validationResponse.setException(ex.getMessage());
            return validationResponse;
        }
    }
}
