package com.jtravan.model;

import org.springframework.http.MediaType;

public class FhirMediaType extends MediaType {

    /**
     * Public constant media type for {@code application/fhir+json}.
     * @see #APPLICATION_FHIR_JSON
     */
    public static final MediaType APPLICATION_FHIR_JSON;

    /**
     * A String equivalent of {@link FhirMediaType#APPLICATION_FHIR_JSON}.
     * @see #APPLICATION_FHIR_JSON_VALUE
     */
    public static final String APPLICATION_FHIR_JSON_VALUE = "application/fhir+json";

    static {
        APPLICATION_FHIR_JSON = valueOf(APPLICATION_FHIR_JSON_VALUE);
    }

    /**
     * Create a new {@code FhirMediaType} for the given primary type.
     * <p>The {@linkplain #getSubtype() subtype} is set to "&#42;", parameters empty.
     * @param type the primary type
     * @throws IllegalArgumentException if any of the parameters contain illegal characters
     */
    public FhirMediaType(String type) {
        super(type);
    }
}
