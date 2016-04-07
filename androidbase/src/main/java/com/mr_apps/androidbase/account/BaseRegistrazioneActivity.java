package com.mr_apps.androidbase.account;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;

/**
 * Created by denis on 07/04/16.
 */
public abstract class BaseRegistrazioneActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        setToolbar();
    }
}
