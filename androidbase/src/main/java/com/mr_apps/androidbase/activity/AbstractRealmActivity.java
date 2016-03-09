package com.mr_apps.androidbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.realm.Realm;

/**
 * Created by mattia on 09/03/2016.
 *
 * @author Mattia Ruggiero
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
