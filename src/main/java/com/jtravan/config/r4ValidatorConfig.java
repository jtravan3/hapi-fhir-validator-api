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
public class r4ValidatorConfig {

    @Bean
    public FhirContext r4FhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    @DependsOn("r4FhirContext")
    public DefaultProfileValidationSupport r4DefaultProfileValidationSupport() {
        return new DefaultProfileValidationSupport(r4FhirContext());
    }

    @Bean
    @DependsOn("r4FhirContext")
    public InMemoryTerminologyServerValidationSupport r4InMemoryTerminologyServerValidationSupport() {
        return new InMemoryTerminologyServerValidationSupport(r4FhirContext());
    }

    @Bean
    @DependsOn("r4FhirContext")
    public CommonCodeSystemsTerminologyService r4CommonCodeSystemsTerminologyService() {
        return new CommonCodeSystemsTerminologyService(r4FhirContext());
    }

    @Bean
    @DependsOn("r4FhirContext")
    public FhirValidator r4Validator()  {
        FhirContext ctx = r4FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r4DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r4InMemoryTerminologyServerValidationSupport());
        validationSupportChain.addValidationSupport(r4CommonCodeSystemsTerminologyService());

        // Add cache for performance
        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);

        // Create a FhirInstanceValidator and register it to a validator
        FhirValidator validator = ctx.newValidator();
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cache);
        validator.registerValidatorModule(instanceValidator);

        return validator;
    }

    @Bean
    @DependsOn("r4FhirContext")
    public FhirValidator r4Validator_NoCodeSystem()  {
        FhirContext ctx = r4FhirContext();

        // Create a validation support chain
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(r4DefaultProfileValidationSupport());
        validationSupportChain.addValidationSupport(r4InMemoryTerminologyServerValidationSupport());

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
