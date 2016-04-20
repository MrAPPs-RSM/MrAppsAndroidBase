package com.mr_apps.androidbaseproject;

import com.mr_apps.androidbase.account.BaseRegistrazioneActivity;
import com.mr_apps.androidbase.account.ElementInputType;
import com.mr_apps.androidbase.account.ElementName;
import com.mr_apps.androidbase.account.SignUpElement;
import com.mr_apps.androidbase.utils.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattia on 07/04/2016.
 *
 * @author Mattia Ruggiero
 */
public class RegistrazioneActivity extends BaseRegistrazioneActivity {

    @Override
    protected List<SignUpElement> getSignUpElements() {
        SignUpElement name = new SignUpElement(this, ElementName.NOME, ElementInputType.STANDARD, true);
        SignUpElement surname = new SignUpElement(this, ElementName.COGNOME, ElementInputType.STANDARD, true);
        SignUpElement phone = new SignUpElement(this, ElementName.TELEFONO, ElementInputType.PHONE, true);
        SignUpElement mail = new SignUpElement(this, ElementName.EMAIL, ElementInputType.MAIL, true);
        SignUpElement newsletter = new SignUpElement(this, ElementName.NEWSLETTER, ElementInputType.SWITCH, false);
        SignUpElement nation = new SignUpElement(this, ElementName.NAZIONE, ElementInputType.SUBSECTION, false);

        List<SignUpElement> list = new ArrayList<>();

        list.add(name);
        list.add(surname);
        list.add(phone);
        list.add(mail);
        list.add(newsletter);
        list.add(nation);

        return list;
    }

    @Override
    protected void termsConditions() {
        CustomDialog.showOkDialog(this, "Termini e condizioni", "Termini: quindi finisci\n Condizioni: quindi sei freddo");
    }

    @Override
    protected void privacyPolicy() {
        CustomDialog.showOkDialog(this, "Privacy policy", "cosa fai mi spii? Occhio alla mia privacy");
    }

    @Override
    protected void onSignupSuccess() {
        CustomDialog.showOkDialog(this, "Dunque ti sei registrato", "Complimenti. Vuoi un biscottino?");
    }

    @Override
    protected void onSubsectionClick(ElementName name) {
        CustomDialog.showOkDialog(this, "Nazione", "Trinidad & Tobago\nAntartide\nNegrolandia");
    }
}
