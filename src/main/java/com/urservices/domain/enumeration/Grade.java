package com.urservices.domain.enumeration;

/**
 * The Grade enumeration.
 */
public enum Grade {
    M("Monsieur"),
    Mme("Madame"),
    Mlle("Mademoiselle"),
    Ing("Ingenieur"),
    Dr("Docteur"),
    Pr("Professeur");

    private final String value;

    Grade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
