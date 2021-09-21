package com.jtravan.process;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionOutput;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HapiFhirValidator implements Validator {

    private final FhirValidator r2Validator;
    private final FhirValidator r2_1Validator;
    private final FhirValidator r3Validator;
    private final FhirValidator r4Validator;
    private final FhirValidator r5Validator;

    private final FhirValidator r2Validator_NoCodeSystem;
    private final FhirValidator r2_1Validator_NoCodeSystem;
    private final FhirValidator r3Validator_NoCodeSystem;
    private final FhirValidator r4Validator_NoCodeSystem;
    private final FhirValidator r5Validator_NoCodeSystem;

    @Autowired
    public HapiFhirValidator(@NonNull FhirValidator r2Validator,
                             @NonNull FhirValidator r2_1Validator,
                             @NonNull FhirValidator r3Validator,
                             @NonNull FhirValidator r4Validator,
                             @NonNull FhirValidator r5Validator,
                             @NonNull FhirValidator r2Validator_NoCodeSystem,
                             @NonNull FhirValidator r2_1Validator_NoCodeSystem,
                             @NonNull FhirValidator r3Validator_NoCodeSystem,
                             @NonNull FhirValidator r4Validator_NoCodeSystem,
                             @NonNull FhirValidator r5Validator_NoCodeSystem) {
        this.r2Validator = r2Validator;
        this.r2_1Validator = r2_1Validator;
        this.r3Validator = r3Validator;
        this.r4Validator = r4Validator;
        this.r5Validator = r5Validator;

        this.r2Validator_NoCodeSystem = r2Validator_NoCodeSystem;
        this.r2_1Validator_NoCodeSystem = r2_1Validator_NoCodeSystem;
        this.r3Validator_NoCodeSystem = r3Validator_NoCodeSystem;
        this.r4Validator_NoCodeSystem = r4Validator_NoCodeSystem;
        this.r5Validator_NoCodeSystem = r5Validator_NoCodeSystem;
    }

    /**
     * Facilitates the HAPI FHIR framework to execute a resource validation
     *
     * @param executionInput the ExecutionInput object to be used for the validation
     * @return an ExecutionOuput object containing the ValidationResult
     */
    @Override
    public ExecutionOutput validate(ExecutionInput executionInput) throws UnhandledFhirVersionException {

        FhirValidator fhirValidator;

        if (executionInput.getIsCodeSystemIgnored() != null
                && executionInput.getIsCodeSystemIgnored()) {
            switch (executionInput.getVersion()) {
                case DSTU2:
                case DSTU2_HL7ORG:
                    fhirValidator = r2Validator_NoCodeSystem;
                    break;
                case DSTU2_1:
                    fhirValidator = r2_1Validator_NoCodeSystem;
                    break;
                case DSTU3:
                    fhirValidator = r3Validator_NoCodeSystem;
                    break;
                case R4:
                    fhirValidator = r4Validator_NoCodeSystem;
                    break;
                case R5:
                    fhirValidator = r5Validator_NoCodeSystem;
                    break;
                default:
                    throw new UnhandledFhirVersionException("FHIR context version not handled.");
            }
        } else {
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
        }

        // To prevent NPE
        String resource = executionInput.getJsonString() == null || executionInput.getJsonString().equals("{}") ? "" : executionInput.getJsonString();

        // Pass a resource in to be validated. The resource can
        // be an IBaseResource instance, or can be a raw String
        // containing a serialized resource as text.
        ValidationResult result = fhirValidator.validateWithResult(resource);

        ExecutionOutput executionOutput = new Hl7FhirValidatorExecutionOutput();
        executionOutput.setValidationResult(result);
        log.info("Validating FHIR resource with version "
                + executionInput.getVersion().getFhirVersionString()
                + ". Is valid FHIR? "
                + result.isSuccessful()
                + ". Resource JSON: "
                + resource);
        return executionOutput;
    }
}
