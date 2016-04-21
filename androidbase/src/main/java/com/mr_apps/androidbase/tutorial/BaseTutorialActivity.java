package com.mr_apps.androidbase.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.RelativeLayout;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.utils.ThemeUtils;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by denis on 19/04/16.
 */
public abstract class BaseTutorialActivity extends AppCompatActivity {

    ViewPager pagerTutorial;
    CirclePageIndicator pageIndicator;

    public static final String Field_SkipLogin="Field_SkipLogin";

    AppCompatButton login, skip;

    ItemTutorial [] tutorials;

    boolean skipLogin=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        RelativeLayout background=(RelativeLayout) findViewById(R.id.background);

        pagerTutorial= (ViewPager) findViewById(R.id.tutorial_pager);
        pageIndicator= (CirclePageIndicator) findViewById(R.id.indicator);

        pageIndicator.setRadius(getResources().getDimension(R.dimen.default_small_margin_or_padding));

        tutorials= getTutorials();

        TutorialAdapter adapter=new TutorialAdapter(getSupportFragmentManager(), this, tutorials);

        pagerTutorial.setAdapter(adapter);

        pageIndicator.setViewPager(pagerTutorial);

        login= (AppCompatButton) findViewById(R.id.login);
        skip= (AppCompatButton) findViewById(R.id.skip);

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

        skipLogin=getIntent().getBooleanExtra(Field_SkipLogin, true);

        showHideButtons(0);

        login.setBackgroundDrawable(ThemeUtils.getButtonSelector(ContextCompat.getColor(this, R.color.tutorial_button_color), this));
        login.setTextColor(ContextCompat.getColor(this, R.color.tutorial_button_textcolor));
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

        styleViews(background, pagerTutorial, pageIndicator, skip, login);

    }

    public abstract void styleViews(RelativeLayout background, ViewPager pager, CirclePageIndicator indicator, AppCompatButton skip, AppCompatButton login);

    public abstract void login();

    public abstract ItemTutorial [] getTutorials();

    private void showHideButtons(int position) {
        if(position==tutorials.length-1) {
            login.setVisibility(View.VISIBLE);
            skip.setVisibility(View.GONE);
        } else {
            login.setVisibility(View.GONE);
            skip.setVisibility(View.VISIBLE);
        }

        if(!skipLogin)
            skip.setVisibility(View.GONE);
    }


}
