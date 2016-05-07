package com.mr_apps.androidbase.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mr_apps.androidbase.R;

/**
 * Class that manages the Base Activity that every activity should extends
 *
 * @author Denis Brandi
 * @author Mattia Ruggiero
 */
public abstract class AbstractBaseActivity extends PickerActivity {

    private static AbstractBaseActivity instance;

    public Toolbar toolbar;

    @Override
    public void onResume() {
        super.onResume();
        instance = this;
    }

    /**
     * Gets the instance of this activity
     *
     * @return the instance of this activity
     */
    public static AbstractBaseActivity getInstance() {
        return instance;
    }

    /**
     * Sets the toolbar to this activity. IMPORTANT: to use this method the activity layout must have a "Toolbar" tag in its XML
     */
    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Alternative method to sets a toolbar with a back button
     */
    public void setBackButton() {
        setToolbar();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Sets a empty title to the toolbar
     */
    public void setEmptyTitle() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("");
    }

    /**
     * Alternative method to sets a toolbar with a close button that acts like a normal back button
     */
    public void setCloseButton() {
        setBackButton();
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    /**
     * Sets a subtitle to the toolbar
     *
     * @param subtitle the subtitle to set
     */
    public void setSubTitle(String subtitle) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


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
