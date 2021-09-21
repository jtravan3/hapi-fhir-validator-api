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
public class r5ValidatorConfig {

    @Bean
    public FhirContext r5FhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    @DependsOn("r5FhirContext")
    public DefaultProfileValidationSupport r5DefaultProfileValidationSupport() {
        return new DefaultProfileValidationSupport(r5FhirContext());
    }

    @Bean
    @DependsOn("r5FhirContext")
    public InMemoryTerminologyServerValidationSupport r5InMemoryTerminologyServerValidationSupport() {
        return new InMemoryTerminologyServerValidationSupport(r5FhirContext());
    }

    @Bean
    @DependsOn("r5FhirContext")
    public CommonCodeSystemsTerminologyService r5CommonCodeSystemsTerminologyService() {
        return new CommonCodeSystemsTerminologyService(r5FhirContext());
    }

    @Bean
    @DependsOn("r5FhirContext")
    public FhirValidator r5Validator()  {
        FhirContext ctx = r5FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r5DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r5InMemoryTerminologyServerValidationSupport());
        validationSupportChain.addValidationSupport(r5CommonCodeSystemsTerminologyService());

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;
    }

    @Bean
    @DependsOn("r5FhirContext")
    public FhirValidator r5Validator_NoCodeSystem()  {
        FhirContext ctx = r5FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r5DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r5InMemoryTerminologyServerValidationSupport());

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
