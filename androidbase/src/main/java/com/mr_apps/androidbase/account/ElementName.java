package com.mr_apps.androidbase.account;

/**
 * Created by mattia on 18/04/2016.
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

    ElementName(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
