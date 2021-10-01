package com.jtravan.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.validation.FhirValidator;
import com.jtravan.config.loader.ValidatorLoader;
import com.jtravan.config.loader.ValidatorType;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;

@Configuration
public class r2ValidatorConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public FhirContext r2FhirContext() {
        return FhirContext.forDstu2Hl7Org();
    }

    @Bean
    @DependsOn("r2FhirContext")
    public FhirValidator r2Validator() throws IOException, UnhandledFhirVersionException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.STANDARD, FhirVersionEnum.DSTU2_HL7ORG, applicationContext);
    }

    @Bean
    @DependsOn("r2FhirContext")
    public FhirValidator r2Validator_NoCodeSystem() throws UnhandledFhirVersionException, IOException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.NO_CODESYSTEM, FhirVersionEnum.DSTU2_HL7ORG, applicationContext);
    }
}
