package com.jtravan.process;

import com.jtravan.exceptions.UnhandledFhirVersionException;
import com.jtravan.model.ExecutionInput;
import com.jtravan.model.ExecutionOutput;

import java.io.IOException;

public interface Validator {

     ExecutionOutput validate(ExecutionInput executionInput) throws IOException, UnhandledFhirVersionException;
}
