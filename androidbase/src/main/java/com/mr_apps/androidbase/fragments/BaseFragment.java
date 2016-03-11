package com.mr_apps.androidbase.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
 */
public class BaseFragment extends Fragment {

    public void onItemSelected(Object... items){}

    public boolean onItemLongPressed(View view, Object... items){
        return false;
    }
}
