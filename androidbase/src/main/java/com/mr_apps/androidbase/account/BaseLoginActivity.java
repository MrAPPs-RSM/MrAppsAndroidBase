package com.mr_apps.androidbase.account;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseLoginActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbar();

        toolbar.setBackgroundColor(getColorPrimary());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColorPrimaryDark());
        }



    }

    public abstract int getColorPrimary();
    public abstract int getColorPrimaryDark();
    public abstract int getColorAccent();
}
