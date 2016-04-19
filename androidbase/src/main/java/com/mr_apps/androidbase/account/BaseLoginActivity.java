package com.mr_apps.androidbase.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.custom_views.PasswordView;
import com.mr_apps.androidbase.custom_views.WarningTextInputLayout;
import com.mr_apps.androidbase.utils.Utils;

import org.json.JSONObject;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseLoginActivity extends AbstractBaseActivity {

    CallbackManager callbackManager;

    TextInputEditText email;
    PasswordView password;
    LoginButton loginButton;
    AppCompatTextView forgetPwd, subscribe, login;
    WarningTextInputLayout til_email, til_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        setToolbar();

        loginButton = (LoginButton) findViewById(R.id.fb_login);

        loginButton.setReadPermissions(getFbPermissions());

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                if (loginResult != null) {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                            if (jsonObject != null) {
                                onFbSuccess(jsonObject, loginResult);
                            } else {
                                LoginManager.getInstance().logOut();
                                onFbError();
                            }

                        }
                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", getFbParameters());
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {
                    onFbError();
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        LoginManager.getInstance().logOut();


        email = (TextInputEditText) findViewById(R.id.email);
        password = (PasswordView) findViewById(R.id.password);
        til_email = (WarningTextInputLayout) findViewById(R.id.til_email);
        til_password = (WarningTextInputLayout) findViewById(R.id.til_password);

        login = (AppCompatTextView) findViewById(R.id.login);

        forgetPwd = (AppCompatTextView) findViewById(R.id.forget_pwd);
        subscribe = (AppCompatTextView) findViewById(R.id.iscriviti);

        login.setBackgroundDrawable(Utils.getButtonSelector(ContextCompat.getColor(this, R.color.colorAccent), this));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    showErrorMessage();
                } else {
                    onLoginSuccess();
                }
            }
        });

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordForgotten();
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_email.setErrorEnabled(!Utils.isValidEmail(s) && s.length() > 0);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctColor(true);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    correctColor(true);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_password.setErrorEnabled(!passwordTilRule(s.toString()) && s.length() > 0);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctColor(false);
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    correctColor(false);
            }
        });
    }

    private void correctColor(final boolean email) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (email) {
                    til_email.setErrorEnabled(til_email.isErrorEnabled());
                } else {
                    til_password.setErrorEnabled(til_password.isErrorEnabled());
                }

            }
        }, 100);
    }

    private boolean checkForm() {
        return til_email.isErrorEnabled() || til_password.isErrorEnabled()
                || email.getText().toString().length() == 0 || password.getText().toString().length() == 0;
    }

    private void showErrorMessage() {
        Snackbar.make(email, R.string.Controllare_dati_inseriti, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public abstract String[] getFbPermissions();

    public abstract String getFbParameters();

    public abstract void onFbSuccess(JSONObject jsonObject, LoginResult loginResult);

    public abstract void onFbError();

    public abstract boolean passwordTilRule(String s);

    protected abstract void onLoginSuccess();

    protected abstract void passwordForgotten();

    protected abstract void subscribe();
}
