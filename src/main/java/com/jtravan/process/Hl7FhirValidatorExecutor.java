package com.jtravan.process;

import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;
import com.jtravan.model.Hl7FhirValidatorExecutionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Hl7FhirValidatorExecutor implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(Hl7FhirValidatorExecutor.class);

    /**
     * For reference: http://wiki.hl7.org/index.php?title=Using_the_FHIR_Validator
     * @return an Hl7FhirValidatorExecutionOutput with the standard out and error from the process execution
     * @throws IOException
     */
    @Override
    @Deprecated
    public ExecutionOutput validate(ExecutionInput executionInput) throws IOException {

        StringBuilder jarCommand = new StringBuilder();
        jarCommand.append("java -jar org.hl7.fhir.validator.jar ")
                .append(executionInput.getJson().getPath())
                .append(" -version ");

        switch (executionInput.getVersion()) {
            case R4:
                jarCommand.append("4.0");
                break;
            case DSTU2:
            case DSTU2_1:
            case DSTU2_HL7ORG:
                jarCommand.append("2.0");
                break;
            case DSTU3:
                jarCommand.append("3.0");
                break;
        }

        Process proc = Runtime.getRuntime().exec(jarCommand.toString());

        BufferedReader stdOutput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        String s;
        StringBuilder stdOut = new StringBuilder();
        while ((s = stdOutput.readLine()) != null) {
            LOG.info(s);
            stdOut.append(s);
        }
        stdOutput.close();

        // read any errors from the attempted command
        StringBuilder stdErr = new StringBuilder();
        while ((s = stdError.readLine()) != null) {
            stdErr.append(s);
        }
        stdError.close();

        Hl7FhirValidatorExecutionOutput executionOutput = new Hl7FhirValidatorExecutionOutput();
        executionOutput.setStandardOutput(stdOut.toString());
        executionOutput.setStandardError(stdErr.toString());
        return executionOutput;
    }


}
