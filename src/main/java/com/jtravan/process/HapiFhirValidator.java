package com.jtravan.process;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.newrelic.api.agent.Trace;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HapiFhirValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(HapiFhirValidator.class);

    private final FhirValidator r2Validator;
    private final FhirValidator r2_1Validator;
    private final FhirValidator r3Validator;
    private final FhirValidator r4Validator;
    private final FhirValidator r5Validator;

    @Autowired
    public HapiFhirValidator(FhirValidator r2Validator,
                             FhirValidator r2_1Validator,
                             FhirValidator r3Validator,
                             FhirValidator r4Validator,
                             FhirValidator r5Validator) {
        this.r2Validator = r2Validator;
        this.r2_1Validator = r2_1Validator;
        this.r3Validator = r3Validator;
        this.r4Validator = r4Validator;
        this.r5Validator = r5Validator;
    }

    /**
     * Facilitates the HAPI FHIR framework to execute a resource validation
     *
     * @param executionInput the ExecutionInput object to be used for the validation
     * @return an ExecutionOuput object containing the ValidationResult
     */
    @Override
    @Trace
    public ExecutionOutput validate(ExecutionInput executionInput) throws UnhandledFhirVersionException {

        FhirValidator fhirValidator;

        switch (executionInput.getVersion()) {
            case DSTU2:
            case DSTU2_HL7ORG:
                fhirValidator = r2Validator;
                break;
            case DSTU2_1:
                fhirValidator = r2_1Validator;
                break;
            case DSTU3:
                fhirValidator = r3Validator;
                break;
            case R4:
                fhirValidator = r4Validator;
                break;
            case R5:
                fhirValidator = r5Validator;
                break;
            default:
                throw new UnhandledFhirVersionException("FHIR context version not handled.");
        }

        // To prevent NPE
        String resource = executionInput.getJsonString() == null || executionInput.getJsonString().equals("{}") ? "" : executionInput.getJsonString();

        // Pass a resource in to be validated. The resource can
        // be an IBaseResource instance, or can be a raw String
        // containing a serialized resource as text.
        ValidationResult result = fhirValidator.validateWithResult(resource);

        ExecutionOutput executionOutput = new Hl7FhirValidatorExecutionOutput();
        executionOutput.setValidationResult(result);
        logger.info("Validating FHIR resource with version "
                + executionInput.getVersion().getFhirVersionString()
                + ". Is valid FHIR? "
                + result.isSuccessful()
                + ". Resource JSON: "
                + resource);
        return executionOutput;
    }
}
