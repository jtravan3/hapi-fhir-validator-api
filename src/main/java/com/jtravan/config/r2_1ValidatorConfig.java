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
public class r2_1ValidatorConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public FhirContext r2_1FhirContext() {
        return FhirContext.forDstu2_1();
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public FhirValidator r2_1Validator() throws UnhandledFhirVersionException, IOException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.STANDARD, FhirVersionEnum.DSTU2_1, applicationContext);
    }

    @Bean
    @DependsOn("r2_1FhirContext")
    public FhirValidator r2_1Validator_NoCodeSystem() throws UnhandledFhirVersionException, IOException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.NO_CODESYSTEM, FhirVersionEnum.DSTU2_1, applicationContext);
    }
}
