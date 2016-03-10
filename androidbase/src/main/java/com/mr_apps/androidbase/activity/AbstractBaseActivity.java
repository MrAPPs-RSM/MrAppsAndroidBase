package com.mr_apps.androidbase.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mr_apps.androidbase.R;

/**
 * Created by denis on 09/02/16
 */
public abstract class AbstractBaseActivity extends PickerActivity {

    private static AbstractBaseActivity instance;

    @Override
    public void onResume() {
        super.onResume();
        instance = this;
    }

    public static AbstractBaseActivity getInstance() {
        return instance;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setBackButton() {
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setEmptyTitle() {
        getSupportActionBar().setTitle("");
    }

    public void setCloseButton() {
        setBackButton();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setSubTitle(String subtitle) {
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

    public void onItemSelected(Object... items) {
    }

    public boolean onItemLongPressed(View view, Object... items) {
        return false;
    }
}
