package com.mr_apps.androidbase.account;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.utils.TextViewUtils;
import com.mr_apps.androidbase.utils.ThemeUtils;

import java.util.List;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseRegistrazioneActivity extends AbstractBaseActivity implements TextViewUtils.ClickableSpannableCallback {

    LinearLayout container;

    AppCompatTextView termsConditions;
    AppCompatTextView signUp;

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

        signUp.setBackgroundDrawable(ThemeUtils.getButtonSelector(ContextCompat.getColor(this, R.color.colorAccent), this));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        List<SignUpElement> elements = getSignUpElements();

        for (SignUpElement el : elements)
            container.addView(el.getView());
    }

    protected abstract List<SignUpElement> getSignUpElements();

    protected abstract void termsConditions();

    protected abstract void privacyPolicy();
}
