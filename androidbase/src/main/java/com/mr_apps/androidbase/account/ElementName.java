package com.mr_apps.androidbase.account;

/**
 * Enum that manages the name of the possible elements of the sign up form
 *
 * @author Mattia Ruggiero
 */
public enum ElementName {

    NOME ("Nome"),
    COGNOME ("Cognome"),
    TELEFONO ("Telefono"),
    EMAIL ("E-Mail"),
    NEWSLETTER ("Newsletter"),
    NAZIONE ("Nazione");

    private final String name;

    /**
     * Constructor that takes the name of the enum
     *
     * @param s the name of the enum
     */
    ElementName(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
