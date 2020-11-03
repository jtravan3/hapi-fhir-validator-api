package com.jtravan.process;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import com.jtravan.config.HAPIValidatorConfig;
import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionInput;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(classes = {HapiFhirValidator.class, HAPIValidatorConfig.class})
@RunWith(SpringRunner.class)
@ContextConfiguration
public class HapiFhirValidatorTest {

    private static final Logger logger = LoggerFactory.getLogger(HapiFhirValidatorTest.class);

    @Autowired
    private HapiFhirValidator hapiFhirValidator;

    @Test
    public void testR2Validator() throws Exception {
        testValidator("classpath:r2_Observation.json", FhirVersionEnum.DSTU2);
    }

    @Test
    @Ignore // Weird results right now
    public void testR2_1Validator() throws Exception {
        testValidator("classpath:r2_1_Observation.json", FhirVersionEnum.DSTU2_1);
    }

    @Test
    public void testR3Validator() throws Exception {
        testValidator("classpath:r3_Observation.json", FhirVersionEnum.DSTU3);
    }

    @Test
    public void testR4Validator() throws Exception {
        testValidator("classpath:r4_Observation.json", FhirVersionEnum.R4);
    }

    @Test
    public void testR5Validator() throws Exception {
        testValidator("classpath:r5_Observation.json", FhirVersionEnum.R5);
    }

    /**
     * Common method to spin up the validator with a string input and assertion
     * @param path the String path for the JSON FHIR file
     * @param version the FHIR version to validate against
     * @throws IOException
     * @throws UnhandledFhirVersionException
     */
    private void testValidator(String path, FhirVersionEnum version) throws IOException, UnhandledFhirVersionException {
        Path filePath = ResourceUtils.getFile(path).toPath();
        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        ExecutionInput executionInput = new Hl7FhirValidatorExecutionInput();
        executionInput.setJsonString(content);
        executionInput.setVersion(version);
        ExecutionOutput executionOutput = hapiFhirValidator.validate(executionInput);
        if (executionOutput.getValidationResult().isSuccessful()) {
            Assert.assertTrue(true);
        } else {
            for (SingleValidationMessage message : executionOutput.getValidationResult().getMessages()) {
                logger.error(message.getMessage());
            }
            Assert.fail();
        }
    }
}
