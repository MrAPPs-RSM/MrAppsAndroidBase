package com.mr_apps.androidbase.utils;

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
 * Created by denis on 20/04/16.
 */
public class DrawableUtils {

    public static Drawable getVectorDrawable(Context context, int resId) {
        return Build.VERSION.SDK_INT >= 21 ? ContextCompat.getDrawable(context, resId) : VectorDrawableCompat.create(context.getResources(), resId, null);
    }

    public static StateListDrawable getSelector(String color)
    {
        StateListDrawable states= new StateListDrawable();

        int col= Color.parseColor(color);

        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2]= (float) (1.0-0.3);

        int press=Color.HSVToColor(hsv);

        int statePressed = android.R.attr.state_pressed;

        states.addState(new int[]{-statePressed}, new ColorDrawable(col));

        states.addState(new int[]{statePressed}, new ColorDrawable(press));

        return states;
    }

    public static StateListDrawable getSelector(int col)
    {
        StateListDrawable states= new StateListDrawable();

        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2]= (float) (hsv[2]-0.1);

        int press=Color.HSVToColor(hsv);

        int statePressed = android.R.attr.state_pressed;

        states.addState(new int[]{-statePressed}, new ColorDrawable(col));

        states.addState(new int[]{statePressed}, new ColorDrawable(press));

        return states;
    }

    /**
     * Metodo di utility per creare drawable con effetto pressed e colori parametrizzati
     * @param color il colore "standard" del bottone
     * @param context il context
     * @return un drawable del colore passato come parametro con bordi arrotondati (3dp) e effetto press
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
     * Metodo che "scurisce" il colore passato come parametro. Utile per creare effetto "press"
     * @param col il colore che si vuole scurire
     * @return il colore passato come parametro con una tonalità più scura
     */
    public static int getDarker(int col) {
        float[] hsv = new float[3];

        Color.colorToHSV(col, hsv);

        hsv[2] = (float) (hsv[2]-0.1);

        return Color.HSVToColor(hsv);
    }

}
