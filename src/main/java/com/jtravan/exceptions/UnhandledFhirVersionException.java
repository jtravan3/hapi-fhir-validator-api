package com.jtravan.exceptions;

public class UnhandledFhirVersionException extends Exception {

    public UnhandledFhirVersionException(String message) {
        super(message);
    }
}
