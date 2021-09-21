package com.jtravan.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class r3ValidatorConfig {

    @Bean
    public FhirContext r3FhirContext() {
        return FhirContext.forDstu3();
    }

    @Bean
    @DependsOn("r3FhirContext")
    public DefaultProfileValidationSupport r3DefaultProfileValidationSupport() {
        return new DefaultProfileValidationSupport(r3FhirContext());
    }

    @Bean
    @DependsOn("r3FhirContext")
    public InMemoryTerminologyServerValidationSupport r3InMemoryTerminologyServerValidationSupport() {
        return new InMemoryTerminologyServerValidationSupport(r3FhirContext());
    }

    @Bean
    @DependsOn("r3FhirContext")
    public CommonCodeSystemsTerminologyService r3CommonCodeSystemsTerminologyService() {
        return new CommonCodeSystemsTerminologyService(r3FhirContext());
    }

    @Bean
    @DependsOn("r3FhirContext")
    public FhirValidator r3Validator()  {
        FhirContext ctx = r3FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r3DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r3InMemoryTerminologyServerValidationSupport());
        validationSupportChain.addValidationSupport(r3CommonCodeSystemsTerminologyService());

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;
    }

    @Bean
    @DependsOn("r3FhirContext")
    public FhirValidator r3Validator_NoCodeSystem()  {
        FhirContext ctx = r3FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r3DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r3InMemoryTerminologyServerValidationSupport());

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
}
