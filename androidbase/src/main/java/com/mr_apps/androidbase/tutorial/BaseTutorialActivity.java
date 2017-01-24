package com.mr_apps.androidbase.tutorial;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.RelativeLayout;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbaseutils.DrawableUtils;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Base abstract activity that should be extended to create a tutorial for an application
 *
 * @author Denis Brandi
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

        Drawable d = DrawableUtils.getVectorDrawable(this, R.drawable.ic_chevron_right_white_18dp);

        skip.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);

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

        login.setBackgroundDrawable(DrawableUtils.getButtonSelector(ContextCompat.getColor(this, R.color.tutorial_button_color), this));
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

    /**
     * Abstract method that should be override by the subclasses to style all the views of the tutorial
     *
     * @param background the layout of the background
     * @param pager the view pager
     * @param indicator the view pager indicator
     * @param skip the button "skip" to skip the turorial
     * @param login the "login" button to show at the end of the tutorial
     */
    public abstract void styleViews(RelativeLayout background, ViewPager pager, CirclePageIndicator indicator, AppCompatButton skip, AppCompatButton login);

    /**
     * Abstract method that should be override by the subclasses to manage the actions to do on "login" button tapped
     */
    public abstract void login();

    /**
     * Abstract method that should be override by the subclasses to gets all the items of this tutorial
     *
     * @return an array containing all the items of the tutorial
     */
    public abstract ItemTutorial [] getTutorials();

    /**
     * Shows or hides the buttons base on the given position
     *
     * @param position the position of the current "slide" of the tutorial
     */
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
