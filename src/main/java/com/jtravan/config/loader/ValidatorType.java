package com.jtravan.config.loader;

import ca.uhn.fhir.util.CoverageIgnore;

import java.util.HashMap;
import java.util.Map;

@CoverageIgnore
public enum ValidatorType {
    STANDARD("standard"),
    NO_CODESYSTEM("no_codesystem");

    private static Map<String, ValidatorType> TYPE_TO_ENUM = new HashMap<>();

    static {
        for (ValidatorType next : ValidatorType.values()) {
            TYPE_TO_ENUM.put(next.getType(), next);
        }
    }

    private final String myType;

    ValidatorType(String theType) {
        myType = theType;
    }

    /**
     * Gets the value from an incoming string
     *
     * @param theCode
     * @return enum value
     */
    public static ValidatorType fromString(String theCode) {
        for (ValidatorType o : ValidatorType.values()) {
            if (o.myType.equalsIgnoreCase(theCode)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Returns the enumerated value associated with this code
     */
    public ValidatorType forType(String myType) {
        ValidatorType retVal = TYPE_TO_ENUM.get(myType);
        return retVal;
    }

    /**
     * Returns the code associated with this enumerated value
     */
    public String getType() {
        return myType;
    }

}
