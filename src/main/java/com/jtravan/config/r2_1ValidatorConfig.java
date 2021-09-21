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
public class r2_1ValidatorConfig {

    @Bean
    public FhirContext r2_1FhirContext() {
        return FhirContext.forDstu2_1();
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public DefaultProfileValidationSupport r2_1DefaultProfileValidationSupport() {
        return new DefaultProfileValidationSupport(r2_1FhirContext());
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public InMemoryTerminologyServerValidationSupport r2_1InMemoryTerminologyServerValidationSupport() {
        return new InMemoryTerminologyServerValidationSupport(r2_1FhirContext());
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public CommonCodeSystemsTerminologyService r2_1CommonCodeSystemsTerminologyService() {
        return new CommonCodeSystemsTerminologyService(r2_1FhirContext());
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public FhirValidator r2_1Validator()  {
        FhirContext ctx = r2_1FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r2_1DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r2_1InMemoryTerminologyServerValidationSupport());
        validationSupportChain.addValidationSupport(r2_1CommonCodeSystemsTerminologyService());

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public FhirValidator r2_1Validator_NoCodeSystem()  {
        FhirContext ctx = r2_1FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r2_1DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r2_1InMemoryTerminologyServerValidationSupport());

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
