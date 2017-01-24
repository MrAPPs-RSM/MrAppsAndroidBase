package com.mr_apps.androidbaseutils.utils;

import android.support.design.widget.AppBarLayout;

/**
 * Class that represents a listener that fires the "onStateChanged" every time the App Bar changes its state.
 * The 3 states of the App Bar are EXPANDED (when it's fully expanded), COLLAPSED (when it's fully collapsed), IDLE (all the other cases)
 *
 * @author Mattia Ruggiero
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    /**
     * Method to be fired every time the App Bar changes state
     *
     * @param appBarLayout the App Bar that changed state
     * @param state        the new state of the App Bar
     */
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
