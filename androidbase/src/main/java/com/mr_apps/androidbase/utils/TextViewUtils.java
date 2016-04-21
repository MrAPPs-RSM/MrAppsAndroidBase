package com.mr_apps.androidbase.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by denis on 21/04/16.
 */
public class TextViewUtils {

    public interface ClickableSpannableCallback {
        void onSpanClicked(int index);
    }

    public static SpannableStringBuilder getSpannableString(final ClickableSpannableCallback callback, final int color, String fullString, String... pieces) {
        return getSpannableString(callback, color, false, false, fullString, pieces);
    }

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

    private static ArrayList<String> splitStringByPieces(String fullString, String[] pieces) {

        ArrayList<String> list = new ArrayList<>();

        String subStringed = fullString;

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
