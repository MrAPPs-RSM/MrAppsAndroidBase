package com.mr_apps.androidbaseproject;

import android.content.Intent;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mr_apps.androidbase.account.BaseLoginActivity;
import com.mr_apps.androidbase.utils.CustomDialog;
import com.mr_apps.androidbase.utils.Logger;

import org.json.JSONObject;

/**
 * Created by denis on 07/04/16.
 */
public class LoginActivity extends BaseLoginActivity {

    private static final String TAG = "LoginActivity";

    @Override
    public String[] getFbPermissions() {
        return new String[]{"user_birthday", "email"};
    }

    @Override
    public String getFbParameters() {
        return "id, name, email,gender, birthday, first_name, last_name";
    }

    @Override
    public void onFbSuccess(JSONObject jsonObject, LoginResult loginResult) {

        Logger.d(TAG, jsonObject.toString());

    }

    @Override
    public void onFbError() {

    }

    @Override
    public boolean passwordTilRule(String s) {
        return s.length() >= 4;
    }

    @Override
    protected void onLoginSuccess() {
        CustomDialog.showOkDialog(this, "Complimenti!", "Ti sei loggato!");
    }

    @Override
    protected void passwordForgotten() {
        CustomDialog.showOkDialog(this, "Ti sei dimenticato la password?", "Problemi tuoi");
    }

    @Override
    protected void subscribe() {
        startActivity(new Intent(this, RegistrazioneActivity.class));
    }

    @Override
    protected void onGoogleSignInCompleted(GoogleSignInAccount account) {
        if (account == null) {
            CustomDialog.showOkDialog(this, "Hai cannato qualcosa", "Login fallito");
        } else {
            CustomDialog.showOkDialog(this, "SUCCESSO!", "Login riuscito con account:\n" + account.getEmail());
        }
    }
}
