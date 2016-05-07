package com.mr_apps.androidbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.realm.Realm;

/**
 * Class that manages the Base Activity that every activity that uses Realm database should extends
 *
 * @author Denis Brandi
 */
public class AbstractRealmActivity extends AbstractBaseActivity {

    public Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
