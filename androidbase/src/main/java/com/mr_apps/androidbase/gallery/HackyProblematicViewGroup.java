package com.mr_apps.androidbase.gallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Workaround class that resolves an issue of the ViewPager with the FullImageAdapter
 *
 * @author Denis Brandi
 */
public class HackyProblematicViewGroup extends ViewPager {

    /**
     * Constructor that takes the context
     *
     * @param context the context
     */
    public HackyProblematicViewGroup(Context context) {
        super(context);
    }

    /**
     * Constructor that takes the context and the attribute sets of the object
     *
     * @param context the context
     * @param attrs the attribute set
     */
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
