package com.mr_apps.androidbase.custom_views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mr_apps.androidbase.R;

/**
 * Created by mattia on 08/04/2016.
 *
 * @author Mattia Ruggiero
 */
public class WarningTextInputLayout extends TextInputLayout {

    boolean isErrorEnabled = false;

    public WarningTextInputLayout(Context context) {
        super(context);
    }

    public WarningTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        EditText edit = getEditText();
        if (edit != null) {
            if (enabled) {
                edit.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                setHintTextAppearance(R.style.HintErrorStyle);
                if (edit instanceof PasswordView) {
                    passwordViewManagement(edit, true);
                } else {
                    Drawable warning = ContextCompat.getDrawable(getContext(), R.drawable.ic_warning_24dp);
                    warning.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, warning, null);
                }
            } else {
                edit.getBackground().clearColorFilter();
                setHintTextAppearance(R.style.HintStyle);
                if (edit instanceof PasswordView) {
                    passwordViewManagement(edit, false);
                } else {
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
            isErrorEnabled = enabled;
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return isErrorEnabled;
    }

    private void passwordViewManagement(EditText edit, boolean error) {
        ((PasswordView) edit).setError(error);
        Drawable eye = edit.getCompoundDrawables()[2];

        if (eye != null)
            if (error) {
                eye.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
            } else {
                eye.clearColorFilter();
            }
    }
}
