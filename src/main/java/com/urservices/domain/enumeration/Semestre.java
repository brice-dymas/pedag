package com.urservices.domain.enumeration;

/**
 * The Semestre enumeration.
 */
public enum Semestre {
    SEMESTRE1("Semestre LMD 1"),
    SEMESTRE2("Semestre LMD 2"),
    SEMESTRE3("Semestre LMD 3"),
    SEMESTRE4("Semestre LMD 4"),
    SEMESTRE5("Semestre LMD 5"),
    SEMESTRE6("Semestre LMD 6");

    private final String value;

    Semestre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
