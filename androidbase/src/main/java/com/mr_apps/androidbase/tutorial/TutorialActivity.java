package com.mr_apps.androidbase.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.account.BaseLoginActivity;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by denis on 19/04/16.
 */
public class TutorialActivity extends AbstractBaseActivity {

    ViewPager pagerTutorial;
    CirclePageIndicator pageIndicator;

    public static final String Field_Tutorials="Field_Tutorials";
    public static final String Field_SkipLogin="Field_SkipLogin";

    Button login, skip;

    ItemTutorial [] tutorials;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        pagerTutorial= (ViewPager) findViewById(R.id.tutorial_pager);
        pageIndicator= (CirclePageIndicator) findViewById(R.id.indicator);

        tutorials= (ItemTutorial[]) getIntent().getSerializableExtra(Field_Tutorials);

        TutorialAdapter adapter=new TutorialAdapter(getSupportFragmentManager(), this, tutorials);

        pagerTutorial.setAdapter(adapter);

        pageIndicator.setViewPager(pagerTutorial);

        login= (Button) findViewById(R.id.login);
        skip= (Button) findViewById(R.id.skip);

        pagerTutorial.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

               showHideButtons(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showHideButtons(0);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        startActivity(new Intent(this, BaseLoginActivity.class));
    }

    private void showHideButtons(int position) {
        if(position==tutorials.length-1) {
            login.setVisibility(View.VISIBLE);
            skip.setVisibility(View.GONE);
        } else {
            login.setVisibility(View.GONE);
            skip.setVisibility(View.VISIBLE);
        }
    }


}
