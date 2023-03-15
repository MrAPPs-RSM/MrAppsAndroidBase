package com.mr_apps.androidbase.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.view.View;

import com.mr_apps.androidbasecore.R;

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
        goTo("http://www.mr-apps.com/it/sviluppo-app");
    }

    public void development(View v) {
        goTo("http://www.mr-apps.com/it/servizi/realizzazione-app");
    }

    public void realization(View v) {
        goTo("http://www.mr-apps.com/it/servizi/realizzazione-siti-web");
    }

    public void design(View v) {
        goTo("http://www.mr-apps.com/it/servizi/realizzazione-ecommerce");
    }

    public void apps(View v) {
        goTo("http://www.mr-apps.com/it/portfolio/progetti");
    }

    public void contacts(View v) {
        goTo("http://www.mr-apps.com/it/contatti");
    }

    public void requestQuote(View v) {
        goTo("http://www.mr-apps.com/it/il-tuo-progetto");
    }

    private void goTo(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }

}
