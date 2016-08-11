package com.mr_apps.androidbase.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;

import com.mr_apps.androidbase.R;

/**
 * Created by denis on 11/08/16.
 */
public class CreditsActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        setBackButton();

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setExpandedTitleColor(Color.TRANSPARENT);

    }

    public void discover(View v) {
        goTo("http://www.mr-apps.com");
    }

    public void development(View v) {
        goTo("http://www.mr-apps.com/servizi/sviluppo-app-san-marino");
    }

    public void realization(View v) {
        goTo("http://www.mr-apps.com/servizi/realizzazione-siti-web-san-marino");
    }

    public void design(View v) {
        goTo("http://www.mr-apps.com/servizi/realizzazione-ecommerce-san-marino");
    }

    public void apps(View v) {
        goTo("http://www.mr-apps.com/portfolio");
    }

    public void contacts(View v) {
        goTo("http://www.mr-apps.com/contact");
    }

    private void goTo(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }

}
