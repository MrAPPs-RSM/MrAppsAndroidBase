package com.mr_apps.androidbaseproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;

import com.facebook.login.LoginResult;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialog.showEditDialog(this, "boia", "qualcosa", "messaggio di merda", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, null, null, new CustomDialog.CustomDialogCallback() {
            @Override
            public void onPositive(EditText editText) {

            }
        });
    }

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
}
