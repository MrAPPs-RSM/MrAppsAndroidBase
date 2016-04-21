package com.mr_apps.androidbase.account;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.custom_views.WarningTextInputLayout;
import com.mr_apps.androidbase.utils.DrawableUtils;
import com.mr_apps.androidbase.utils.TextViewUtils;

import java.util.List;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseRegistrazioneActivity extends AbstractBaseActivity implements TextViewUtils.ClickableSpannableCallback {

    LinearLayout container;

    AppCompatTextView termsConditions;
    AppCompatTextView signUp;

    List<SignUpElement> elements;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        setBackButton();

        container = (LinearLayout) findViewById(R.id.views_container);

        termsConditions = (AppCompatTextView) findViewById(R.id.termini_condizioni);
        signUp = (AppCompatTextView) findViewById(R.id.signup);

        termsConditions.setText(TextViewUtils.getSpannableString(this, Color.BLACK, getString(R.string.termini_condizioni), getString(R.string.termini_condizioni_2), getString(R.string.termini_condizioni_4)));//getTermsConditionSpannableString());
        termsConditions.setMovementMethod(LinkMovementMethod.getInstance());

        signUp.setBackgroundDrawable(DrawableUtils.getButtonSelector(ContextCompat.getColor(this, R.color.colorAccent), this));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    showErrorMessage();
                } else {
                    onSignupSuccess();
                }
            }
        });

        elements = getSignUpElements();

        for (final SignUpElement el : elements) {
            container.addView(el.getView());
            if (el.getInputType().equals(ElementInputType.SUBSECTION)) {
                el.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSubsectionClick(el.getName());
                    }
                });
            }
        }
    }

    protected void onSubsectionClick(ElementName name) {

    }

    private boolean checkForm() {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).isRequired()) {
                View view = container.getChildAt(i);
                TextInputEditText edit = (TextInputEditText) view.findViewById(R.id.standard_name);
                WarningTextInputLayout tilEdit = (WarningTextInputLayout) view.findViewById(R.id.til_name);
                if (edit != null && tilEdit != null && (edit.getText().toString().length() == 0 || tilEdit.isErrorEnabled()))
                    return true;
            }
        }

        return false;
    }

    private void showErrorMessage() {
        Snackbar.make(container, R.string.Campi_obbligatori_non_inseriti, Snackbar.LENGTH_LONG).show();
    }

    protected abstract List<SignUpElement> getSignUpElements();

    protected abstract void termsConditions();

    protected abstract void privacyPolicy();

    protected abstract void onSignupSuccess();
}
