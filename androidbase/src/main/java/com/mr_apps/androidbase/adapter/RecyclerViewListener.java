package com.mr_apps.androidbase.adapter;

import android.view.View;

/**
 * Created by denis on 11/01/16.
 */
public interface RecyclerViewListener {

    void onItemSelected(Object... items);

    boolean onItemLongPressed(View view, Object... items);

}
