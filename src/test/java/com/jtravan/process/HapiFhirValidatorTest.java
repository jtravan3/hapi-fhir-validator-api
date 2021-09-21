package com.jtravan.process;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import com.jtravan.config.*;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionInput;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ResourceUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(classes = {
        HapiFhirValidator.class,
        r5ValidatorConfig.class,
        r4ValidatorConfig.class,
        r3ValidatorConfig.class,
        r2ValidatorConfig.class,
        r2_1ValidatorConfig.class})
@ContextConfiguration
@Slf4j
public class HapiFhirValidatorTest {

    @Autowired
    private HapiFhirValidator hapiFhirValidator;

    @Test
    @Disabled
    public void testR2Validator() throws Exception {
        testValidator("classpath:r2_Observation.json", FhirVersionEnum.DSTU2, false);
        testValidator("classpath:r2_Observation.json", FhirVersionEnum.DSTU2, true);
    }

    @Test
    @Disabled // Weird results right now
    public void testR2_1Validator() throws Exception {
        testValidator("classpath:r2_1_Observation.json", FhirVersionEnum.DSTU2_1, false);
        testValidator("classpath:r2_1_Observation.json", FhirVersionEnum.DSTU2_1, true);
    }

    @Test
    public void testR3Validator() throws Exception {
        testValidator("classpath:r3_Observation.json", FhirVersionEnum.DSTU3, false);
        testValidator("classpath:r3_Observation.json", FhirVersionEnum.DSTU3, true);
    }

    @Test
    public void testR4Validator() throws Exception {
        testValidator("classpath:r4_Observation.json", FhirVersionEnum.R4, false);
        testValidator("classpath:r4_Observation.json", FhirVersionEnum.R4, true);
    }

    @Test
    public void testR5Validator() throws Exception {
        testValidator("classpath:r5_Observation.json", FhirVersionEnum.R5, false);
        testValidator("classpath:r5_Observation.json", FhirVersionEnum.R5, true);
    }

    /**
     * Common method to spin up the validator with a string input and assertion
     * @param path the String path for the JSON FHIR file
     * @param version the FHIR version to validate against
     * @throws IOException
     * @throws UnhandledFhirVersionException
     */
    private void testValidator(String path, FhirVersionEnum version, Boolean isCodeSystemIgnored) throws IOException, UnhandledFhirVersionException {
        Path filePath = ResourceUtils.getFile(path).toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        ExecutionInput executionInput = new Hl7FhirValidatorExecutionInput();
        executionInput.setJsonString(content);
        executionInput.setVersion(version);
        executionInput.setIsCodeSystemIgnored(isCodeSystemIgnored);
        ExecutionOutput executionOutput = hapiFhirValidator.validate(executionInput);
        if (executionOutput.getValidationResult().isSuccessful()) {
            assertThat(true).isTrue();
        } else {
            for (SingleValidationMessage message : executionOutput.getValidationResult().getMessages()) {
                log.error(message.getMessage());
            }
            fail(version.name() + " failed");
        }
    }
}
