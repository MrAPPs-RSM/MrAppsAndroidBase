package com.mr_apps.androidbaseproject;

import android.content.Intent;

import com.mr_apps.androidbase.tutorial.BaseTutorialActivity;

/**
 * Created by denis on 20/04/16.
 */
public class TutorialActivity extends BaseTutorialActivity {

    @Override
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
