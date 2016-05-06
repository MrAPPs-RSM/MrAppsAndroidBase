package com.mr_apps.androidbase.custom_views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.mr_apps.androidbase.R;

/**
 * Custom view used to create a TextInputLayout that displays a red warning drawable instead of the red text below the edit text
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
                    Drawable warning = Build.VERSION.SDK_INT >= 21 ? ContextCompat.getDrawable(getContext(), R.drawable.ic_warning_24dp) : VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_warning_24dp, null);
                    if (warning != null)
                        warning.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                    edit.setCompoundDrawablesWithIntrinsicBounds(null, null, warning, null);
                }
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    edit.getBackground().clearColorFilter();
                } else {
                    edit.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
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

    /**
     * Special management if the EditText contained by the TextInputLayout is instance of the Password View
     *
     * @param edit the edit text (should be a Password View)
     * @param error sets whether the error si activated or not
     */
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
