package com.mr_apps.androidbase.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mr_apps.androidbase.R;

/**
 * Class that manages the Password View, a custom view based on the implementation of Lisa Wray
 * This class manage a special edit text with a button that can switch between "visible password" and "hidden password"
 *
 * @author Lisa Wray
 * @author Mattia Ruggiero
 */
public class PasswordView extends TextInputEditText {

    private Drawable eye;
    private Drawable eyeWithStrike;
    private final static int VISIBILITY_ENABLED = (int) (255 * .54f); // 54%
    private final static int VISIBILITY_DISABLED = (int) (255 * .38f); // 38%
    private boolean visible = false;
    private boolean useStrikeThrough = false;
    private boolean error = false;
    private Typeface typeface;

    public PasswordView(Context context) {
        super(context);
        init(null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Initializator of the custom view, setting the button and the attributes
     *
     * @param attrs the attributes of the view
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PasswordView,
                    0, 0);
            try {
                useStrikeThrough = a.getBoolean(R.styleable.PasswordView_useStrikeThrough, false);
            } finally {
                a.recycle();
            }
        }


        // Make sure to mutate so that if there are multiple password fields, they can have
        // different visibilities.
        if (Build.VERSION.SDK_INT >= 21) {
            eye = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye).mutate();
            eyeWithStrike = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye_strike).mutate();
        } else {
            eye = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_eye, null).mutate();
            eyeWithStrike = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_eye_strike, null).mutate();
        }

        eyeWithStrike.setAlpha(VISIBILITY_ENABLED);
        setup();
    }

    /**
     * Setup of the custom view, setting the input type, the drawable and the error
     */
    protected void setup() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Drawable drawable = useStrikeThrough && !visible ? eyeWithStrike : eye;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        eye.setAlpha(visible && !useStrikeThrough ? VISIBILITY_ENABLED : VISIBILITY_DISABLED);
        if (error) {
            eye.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
            eyeWithStrike.setColorFilter(ContextCompat.getColor(getContext(), R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
        } else {
            eye.clearColorFilter();
            eyeWithStrike.clearColorFilter();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                && event.getX() >= (getRight() - getCompoundDrawables()[2].getBounds().width())) {
            visible = !visible;
            setup();
            requestFocus();
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setInputType(int type) {
        this.typeface = getTypeface();
        super.setInputType(type);
        setTypeface(typeface);
    }

    /**
     * Sets if the button should turn in the "strikethrough" version or not
     *
     * @param useStrikeThrough true if the button's icon should turns in the "strikethrough", false otherwise
     */
    public void setUseStrikeThrough(boolean useStrikeThrough) {
        this.useStrikeThrough = useStrikeThrough;
    }

    /**
     * Sets if the error is activated or not. When activeted, the button turns red
     *
     * @param error true if the error is active, false otherwise
     */
    public void setError(boolean error) {
        this.error = error;
    }
}
