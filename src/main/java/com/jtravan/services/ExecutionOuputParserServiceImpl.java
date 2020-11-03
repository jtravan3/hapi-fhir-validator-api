package com.jtravan.services;

import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.jtravan.api.fhir.model.ValidationResponse;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ExecutionOuputParserServiceImpl implements ExecutionOutputParserService {

    /**
     * Parses the ValidationResult returned from the FHIR Validator and returns a ValidationResponse object
     * @param validationResult the ValidationResult to be parsed
     * @return a ValidationResponse object
     */
    @Override
    public ValidationResponse parseValidationResult(ValidationResult validationResult) {

        ValidationResponse validationResponse = new ValidationResponse();

        List<SingleValidationMessage> errors = new LinkedList<>();
        List<SingleValidationMessage> warnings = new LinkedList<>();
        List<SingleValidationMessage> info = new LinkedList<>();

        for (SingleValidationMessage message : validationResult.getMessages()) {
            if (message.getSeverity() == ResultSeverityEnum.ERROR || message.getSeverity() == ResultSeverityEnum.FATAL) {
                errors.add(message);
                continue;
            }

            if (message.getSeverity() == ResultSeverityEnum.WARNING) {
                warnings.add(message);
                continue;
            }

            if (message.getSeverity() == ResultSeverityEnum.INFORMATION) {
                info.add(message);
            }
        }

        validationResponse.setErrors(errors);
        validationResponse.setWarnings(warnings);
        validationResponse.setInfo(info);
        validationResponse.setSuccessful(validationResult.isSuccessful());

        return validationResponse;
    }
}
