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
public class r4ValidatorConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public FhirContext r4FhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    @DependsOn("r4FhirContext")
    public FhirValidator r4Validator() throws IOException, UnhandledFhirVersionException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.STANDARD, FhirVersionEnum.R4, applicationContext);
    }

    @Bean
    @DependsOn("r4FhirContext")
    public FhirValidator r4Validator_NoCodeSystem() throws UnhandledFhirVersionException, IOException {
        ValidatorLoader validatorLoader = new ValidatorLoader();
        return validatorLoader.loadValidator(ValidatorType.NO_CODESYSTEM, FhirVersionEnum.R4, applicationContext);
    }
}
