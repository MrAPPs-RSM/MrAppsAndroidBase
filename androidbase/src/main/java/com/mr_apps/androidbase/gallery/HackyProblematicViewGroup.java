package com.mr_apps.androidbase.gallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by denis on 11/02/16.
 */
public class HackyProblematicViewGroup extends ViewPager {


    public HackyProblematicViewGroup(Context context) {
        super(context);
    }

    public HackyProblematicViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            return false;
        }
    }

}
