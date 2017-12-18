package com.mr_apps.androidbaseproject;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.widget.RelativeLayout;

import com.mr_apps.androidbase.tutorial.BaseTutorialActivity;
import com.mr_apps.androidbase.tutorial.ItemTutorial;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by denis on 20/04/16.
 */
public class TutorialActivity extends BaseTutorialActivity {


    @Override
    public void styleViews(RelativeLayout background, ViewPager pager, CircleIndicator indicator, AppCompatButton skip, AppCompatButton login) {

    }

    @Override
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public ItemTutorial[] getTutorials() {
        return new ItemTutorial[]{new ItemTutorial(R.drawable.tutorial_01_android, "titolo 1", "sottotitolo 1"), new ItemTutorial(0, "titolo 2", "sottotitolo 2"), new ItemTutorial(0, "titolo 3", "sottotitolo 3")};
    }
}
