package com.mr_apps.androidbase.account;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.custom_views.WarningTextInputLayout;
import com.mr_apps.androidbase.utils.DrawableUtils;
import com.mr_apps.androidbase.utils.TextViewUtils;
import com.mr_apps.androidbaseaccount.R;

import java.util.List;

/**
 * Abstract Base class that manages a sign up activity, that should be extended by a real Sign Up Activity
 *
 * @author Mattia Ruggiero
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

        termsConditions.setText(TextViewUtils.getSpannableString(this, Color.BLACK, getString(R.string.termini_condizioni), getString(R.string.termini_condizioni_2), getString(R.string.termini_condizioni_4)));
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

    /**
     * Manages the actions to do when a part of the form is tapped by the user. Should be override by the subclasses to customize the behaviour
     *
     * @param name the ElementName of the section tapped
     */
    protected void onSubsectionClick(ElementName name) {
    }

    /**
     * Checks all the dynamic elements of the form and decides if the form is valid or not
     *
     * @return true if the form is valid, false otherwise
     */
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

    /**
     * Shows a snackbar that displays an error message
     */
    private void showErrorMessage() {
        Snackbar.make(container, R.string.Campi_obbligatori_non_inseriti, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Abstract method that should be override by the subclasses to set all the elements (edit text, switch, subsections) of the sign up form
     *
     * @return a list containing all the sign up form elements
     */
    protected abstract List<SignUpElement> getSignUpElements();

    /**
     * Abstract method that should be override by the subclasses to set the actions to do when the "terms & conditions" button is tapped
     */
    protected abstract void termsConditions();

    /**
     * Abstract method that should be override by the subclasses to set the actions to do when the "privacy policy" button is tapped
     */
    protected abstract void privacyPolicy();

    /**
     * Abstract method that should be override by the subclasses to set the actions to do when the "sign up" button is tapped and the sign up is successful
     */
    protected abstract void onSignupSuccess();
}
