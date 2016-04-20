package com.mr_apps.androidbase.account;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.utils.ThemeUtils;

import java.util.List;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseRegistrazioneActivity extends AbstractBaseActivity {

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

        termsConditions.setText(getTermsConditionSpannableString());
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

    private SpannableStringBuilder getTermsConditionSpannableString() {
        int start;
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(getString(R.string.termini_condizioni_1));
        builder.append(" ");

        start = builder.length();
        builder.append(getString(R.string.termini_condizioni_2));
        builder.setSpan(getClickableSpan(true), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ");

        builder.append(getString(R.string.termini_condizioni_3));
        builder.append(" ");

        start = builder.length();
        builder.append(getString(R.string.termini_condizioni_4));
        builder.setSpan(getClickableSpan(false), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ");

        return builder;
    }

    private ClickableSpan getClickableSpan(final boolean termsCondition) {
        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (termsCondition)
                    termsConditions();
                else
                    privacyPolicy();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        };
    }

    protected abstract List<SignUpElement> getSignUpElements();

    protected abstract void termsConditions();

    protected abstract void privacyPolicy();
}
