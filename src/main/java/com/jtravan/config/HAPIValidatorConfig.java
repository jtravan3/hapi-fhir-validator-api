package com.jtravan.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class HAPIValidatorConfig {

    @Bean
    public FhirValidator r5Validator() throws IOException {
        FhirValidator fhirValidator = FhirContext.forR5().newValidator();
        fhirValidator.registerValidatorModule(new FhirInstanceValidator(FhirContext.forR5()));

        // Hacky way to pre-initialize the validator on startup
        Path filePath = ResourceUtils.getFile("classpath:r5_Observation.json").toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        fhirValidator.validateWithResult(content);

        return fhirValidator;
    }

    @Bean
    public FhirValidator r4Validator() throws IOException {
        FhirValidator fhirValidator = FhirContext.forR4().newValidator();
        fhirValidator.registerValidatorModule(new FhirInstanceValidator(FhirContext.forR4()));

        // Hacky way to pre-initialize the validator on startup
        Path filePath = ResourceUtils.getFile("classpath:r4_Observation.json").toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        fhirValidator.validateWithResult(content);

        return fhirValidator;
    }

    @Bean
    public FhirValidator r3Validator() throws IOException {
        FhirValidator fhirValidator = FhirContext.forDstu3().newValidator();
        fhirValidator.registerValidatorModule(new FhirInstanceValidator(FhirContext.forDstu3()));

        // Hacky way to pre-initialize the validator on startup
        Path filePath = ResourceUtils.getFile("classpath:r3_Observation.json").toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        fhirValidator.validateWithResult(content);

        return fhirValidator;
    }

    @Bean
    public FhirValidator r2_1Validator() throws IOException {
        FhirValidator fhirValidator = FhirContext.forDstu2_1().newValidator();
        fhirValidator.registerValidatorModule(new FhirInstanceValidator(FhirContext.forDstu2_1()));

        // Hacky way to pre-initialize the validator on startup
        Path filePath = ResourceUtils.getFile("classpath:r2_1_Observation.json").toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        fhirValidator.validateWithResult(content);

        return fhirValidator;
    }

    @Bean
    public FhirValidator r2Validator() throws IOException {
        FhirValidator fhirValidator = FhirContext.forDstu2Hl7Org().newValidator();
        fhirValidator.registerValidatorModule(new FhirInstanceValidator(FhirContext.forDstu2Hl7Org()));

        // Hacky way to pre-initialize the validator on startup
        Path filePath = ResourceUtils.getFile("classpath:r2_Observation.json").toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        fhirValidator.validateWithResult(content);

        return fhirValidator;
    }
}
