package com.mr_apps.androidbase.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.ArrayList;

/**
 * Class that provides basic utils for TextView
 *
 * @author Denis Brandi
 * @author Mattia Ruggiero
 */
public class TextViewUtils {

    public interface ClickableSpannableCallback {
        void onSpanClicked(int index);
    }


    /**
     * Method that makes a fully customizable clickable string
     *
     * @param callback   the action to be done when a part of the string is clicked
     * @param color      the color of the clickable parts of the string
     * @param fullString the full string that has to be modified
     * @param pieces     optional parameter that contains the pieces of the string that will be clickable
     * @return the spannable string
     */
    public static SpannableStringBuilder getSpannableString(final ClickableSpannableCallback callback, final int color, String fullString, String... pieces) {
        return getSpannableString(callback, color, false, false, fullString, pieces);
    }

    /**
     * Method that makes a fully customizable clickable string
     *
     * @param callback     the action to be done when a part of the string is clicked
     * @param color        the color of the clickable parts of the string
     * @param isBold       sets if the clickable parts of the string are bold or not
     * @param isUnderlined sets if the clickable parts of the string are underlined or not
     * @param fullString   the full string that has to be modified
     * @param pieces       optional parameter that contains the pieces of the string that will be clickable
     * @return the spannable string
     */
    public static SpannableStringBuilder getSpannableString(final ClickableSpannableCallback callback, final int color, final boolean isBold, final boolean isUnderlined, String fullString, String... pieces) {

        ArrayList<String> list = splitStringByPieces(fullString, pieces);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        int start = 0;
        int index = 0;
        for (String s : list) {

            builder.append(s);

            boolean isClickable = false;

            for (String s1 : pieces) {
                if (s.equals(s1)) {
                    isClickable = true;
                    break;
                }
            }

            if (isClickable) {
                final int finalIndex = index;
                builder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        callback.onSpanClicked(finalIndex);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(isUnderlined);
                        ds.setFakeBoldText(isBold);
                        ds.setColor(color);
                    }

                }, start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                index++;
            }

            start = builder.length();
        }

        /*int start;

        builder.append(getString(R.string.termini_condizioni_1));
        builder.append(" ");

        start = builder.length();
        builder.append(getString(R.string.termini_condizioni_2));
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                callback.onSpanClicked(0);
            }
        }, start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ");

        builder.append(getString(R.string.termini_condizioni_3));
        builder.append(" ");

        start = builder.length();
        builder.append(getString(R.string.termini_condizioni_4));
        builder.setSpan(getClickableSpan(false), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" ");*/

        return builder;
    }

    /**
     * Splits the given string using the given pieces to determine the split positions
     *
     * @param fullString the full string that has to be splitted
     * @param pieces     the pieces in which the full string has to be splitted
     * @return an arrayList containing the parts of the splitted string
     */
    private static ArrayList<String> splitStringByPieces(String fullString, String[] pieces) {

        ArrayList<String> list = new ArrayList<>();

        String subStringed = fullString;

        for (String piece : pieces) {
            String[] split = fullString.split(piece);

            if (split[0].equals(fullString)) {
                list.add(fullString);

                for(String string : pieces) {
                    list.add(" ");
                    list.add(string);
                }

                return list;
            }
        }

        for (String s : pieces) {
            String[] subSplitted = subStringed.split(s);

            list.add(subSplitted[0]);
            list.add(s);

            if (subSplitted.length > 1) {
                subStringed = "";
                for (int i = 1; i < subSplitted.length; i++) {
                    subStringed += subSplitted[i];
                }
            }
        }

        return list;

    }

}
