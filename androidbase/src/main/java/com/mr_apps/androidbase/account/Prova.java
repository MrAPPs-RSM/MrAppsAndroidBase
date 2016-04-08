package com.mr_apps.androidbase.account;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.mr_apps.androidbase.R;

/**
 * Created by mattia on 08/04/2016.
 *
 * @author Mattia Ruggiero
 */
public class Prova extends TextInputLayout {

    public Prova(Context context) {
        super(context);
    }

    public Prova(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        if (getEditText() != null) {
            if (enabled) {
                getEditText().getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                setHintTextAppearance(R.style.HintErrorStyle);
                Drawable warning = ContextCompat.getDrawable(getContext(), R.drawable.ic_warning_24dp);
                warning.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, warning, null);
            } else {
                getEditText().getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                setHintTextAppearance(R.style.HintStyle);
                getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }
}
