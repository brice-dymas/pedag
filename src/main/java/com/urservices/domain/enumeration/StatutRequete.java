package com.urservices.domain.enumeration;

/**
 * The StatutRequete enumeration.
 */
public enum StatutRequete {
    EN_ATTENTE("EN ATTENTE"),
    FONDE("FONDÉE"),
    NON_FONDE("NON FONDÉE");

    private final String value;

    StatutRequete(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
