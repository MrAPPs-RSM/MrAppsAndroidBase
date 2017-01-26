package com.mr_apps.androidbase.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Base Fragment of every fragment, containing methods to respond to the custom recycler view's adapter taps
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public class BaseFragment extends Fragment {

    /**
     * Method that should be called by the recycler view's adapter on item tap
     *
     * @param items customizable params
     */
    public void onItemSelected(Object... items) {
    }

    /**
     * Method that should be called by the recycler view's adapter on item long tap
     *
     * @param view  the view tapped
     * @param items customizable params
     * @return true if the tap consumed the action, false otherwise
     */
    public boolean onItemLongPressed(View view, Object... items) {
        return false;
    }
}
