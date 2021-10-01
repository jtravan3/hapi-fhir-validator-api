package com.jtravan.config.loader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.IOException;

public class ValidatorLoader {

    private FhirContext ctx;

    public FhirValidator loadValidator(ValidatorType type, FhirVersionEnum fhirVersionEnum, @Nullable ApplicationContext applicationContext) throws UnhandledFhirVersionException, IOException {

        if (type.equals(type.STANDARD)) {
            return loadStandardValidator(fhirVersionEnum, applicationContext);
        } else {
            return loadNoNoCodeSystemValidator(fhirVersionEnum);
        }
    }

    private FhirValidator loadStandardValidator(FhirVersionEnum fhirVersionEnum, ApplicationContext applicationContext) throws UnhandledFhirVersionException, IOException {
        loadContext(fhirVersionEnum);

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(ctx));
        validationSupportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(ctx));
        validationSupportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ctx));
        validationSupportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(ctx));

        if (fhirVersionEnum.isNewerThan(fhirVersionEnum.DSTU2_1)) {
            NpmPackageValidationSupport npmPackageValidationSupport = new NpmPackageValidationSupport(ctx);
            for (Resource implementationGuide : loadIGs(applicationContext)) {
                npmPackageValidationSupport.loadPackageFromClasspath("classpath:ig/" + implementationGuide.getFilename());
            }

            validationSupportChain.addValidationSupport(npmPackageValidationSupport);
        }

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;

    }

    private FhirValidator loadNoNoCodeSystemValidator(FhirVersionEnum fhirVersionEnum) throws UnhandledFhirVersionException {
        loadContext(fhirVersionEnum);
        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(ctx));
        validationSupportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ctx));

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);

        /*
         * If you want, you can configure settings on the validator to adjust
         * its behaviour during validation
         */
        instanceValidator.setAnyExtensionsAllowed(true);
        instanceValidator.setErrorForUnknownProfiles(false);
        instanceValidator.setAssumeValidRestReferences(true);
        instanceValidator.setNoTerminologyChecks(true);

        validator.registerValidatorModule(instanceValidator);
        return validator;
    }

    private void loadContext(FhirVersionEnum fhirVersionEnum) throws UnhandledFhirVersionException {
        switch (fhirVersionEnum) {
            case DSTU2_1:
                ctx = FhirContext.forDstu2_1();
                break;
            case DSTU2_HL7ORG:
                ctx = FhirContext.forDstu2Hl7Org();
                break;
            case DSTU3:
                ctx = FhirContext.forDstu3();
                break;
            case R4:
                ctx = FhirContext.forR4();
                break;
            case R5:
                ctx = FhirContext.forR5();
                break;
            default:
                throw new UnhandledFhirVersionException("FHIR context version not handled.");
        }
    }


    public Resource[] loadIGs(ApplicationContext applicationContext) throws IOException {
        return applicationContext.getResources("classpath:ig/*.tgz");
    }
}
