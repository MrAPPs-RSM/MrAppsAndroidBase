package com.mr_apps.androidbaseutils.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

/**
 * Class that contains some useful drawable utils
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public class DrawableUtils {

    /**
     * Gets a vector drawable, using the right methods for all the platforms
     *
     * @param context the context
     * @param resId   the resId of the vector drawable
     * @return a drawable converted from the vector
     */
    public static Drawable getVectorDrawable(Context context, int resId) {
        return Build.VERSION.SDK_INT >= 21 ? ContextCompat.getDrawable(context, resId) : VectorDrawableCompat.create(context.getResources(), resId, null);
    }

    /**
     * Gets a selector using the given color, with automatically generated press color
     *
     * @param color the string of the base color of the selector
     * @return a selector with a automatically generated press color
     */
    public static StateListDrawable getSelector(String color) {
        StateListDrawable states = new StateListDrawable();

        int col = Color.parseColor(color);

        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2] = (float) (1.0 - 0.3);

        int press = Color.HSVToColor(hsv);

        int statePressed = android.R.attr.state_pressed;

        states.addState(new int[]{-statePressed}, new ColorDrawable(col));

        states.addState(new int[]{statePressed}, new ColorDrawable(press));

        return states;
    }

    /**
     * Gets a selector using the given color, with automatically generated press color
     *
     * @param col the resource id of the base color of the selector
     * @return a selector with a automatically generated press color
     */
    public static StateListDrawable getSelector(int col) {
        StateListDrawable states = new StateListDrawable();

        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2] = (float) (hsv[2] - 0.1);

        int press = Color.HSVToColor(hsv);

        int statePressed = android.R.attr.state_pressed;

        states.addState(new int[]{-statePressed}, new ColorDrawable(col));

        states.addState(new int[]{statePressed}, new ColorDrawable(press));

        return states;
    }

    /**
     * Creates a drawable with press effect and parameterized colors
     *
     * @param color   the standard color of the button
     * @param context the context
     * @return a drawable with the given color, press effect and rounded corners
     */
    public static StateListDrawable getButtonSelector(int color, Context context) {

        GradientDrawable standardDrawable = new GradientDrawable();
        standardDrawable.setCornerRadius(Utils.dpToPx(3, context));
        standardDrawable.setColor(color);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setCornerRadius(Utils.dpToPx(3, context));
        pressedDrawable.setColor(getDarker(color));

        StateListDrawable states = new StateListDrawable();
        int statePressed = android.R.attr.state_pressed;
        states.addState(new int[]{-statePressed}, standardDrawable);
        states.addState(new int[]{statePressed}, pressedDrawable);

        return states;
    }

    /**
     * Darkens the given color
     *
     * @param col the color to be darken
     * @return the given color with a dark shade
     */
    public static int getDarker(int col) {
        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2] = (float) (hsv[2] - 0.1);

        return Color.HSVToColor(hsv);
    }

}
