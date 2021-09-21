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
public class r2ValidatorConfig {

    @Bean
    public FhirContext r2FhirContext() {
        return FhirContext.forDstu2Hl7Org();
    }

    @Bean
    @DependsOn("r2FhirContext")
    public DefaultProfileValidationSupport r2DefaultProfileValidationSupport() {
        return new DefaultProfileValidationSupport(r2FhirContext());
    }

    @Bean
    @DependsOn("r2FhirContext")
    public InMemoryTerminologyServerValidationSupport r2InMemoryTerminologyServerValidationSupport() {
        return new InMemoryTerminologyServerValidationSupport(r2FhirContext());
    }

    @Bean
    @DependsOn("r2FhirContext")
    public CommonCodeSystemsTerminologyService r2CommonCodeSystemsTerminologyService() {
        return new CommonCodeSystemsTerminologyService(r2FhirContext());
    }

    @Bean
    @DependsOn("r2FhirContext")
    public FhirValidator r2Validator()  {
        FhirContext ctx = r2FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r2DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r2InMemoryTerminologyServerValidationSupport());
        validationSupportChain.addValidationSupport(r2CommonCodeSystemsTerminologyService());

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;
    }

    @Bean
    @DependsOn("r2FhirContext")
    public FhirValidator r2Validator_NoCodeSystem()  {
        FhirContext ctx = r2FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r2DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r2InMemoryTerminologyServerValidationSupport());

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
