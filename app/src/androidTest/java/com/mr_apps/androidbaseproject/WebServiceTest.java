package com.mr_apps.androidbaseproject;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.JsonObject;
import com.mr_apps.androidbase.webservice.BaseLoopJSecurity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.msebera.android.httpclient.concurrent.FutureCallback;

/**
 * Created by denis on 03/08/16.
 */
@RunWith(AndroidJUnit4.class)
public class WebServiceTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public WebServiceTest() {
        super(MainActivity.class);
    }

    Context context=null;

    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context=getActivity();
        testNewApi();
    }

    @Test
    public void testNewApi() {
        BaseLoopJSecurity baseLoopJSecurity=BaseLoopJSecurity.getInstance(new BaseLoopJSecurity() {
            @Override
            public boolean handleErrorCode(Context context, JsonObject result) {
                return super.handleErrorCode(context, result);
            }
        });

        BaseLoopJSecurity.setBaseUrl("http://smoll.mrapps.private");

        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("offset", 0);
        jsonObject.addProperty("limit", 12);

        baseLoopJSecurity.baseOperationWithPath(context, "api/articles/all", jsonObject.toString(), new FutureCallback<JsonObject>() {
            @Override
            public void completed(JsonObject result) {
                assertEquals(true, result!=null);

            }

            @Override
            public void failed(Exception ex) {

            }

            @Override
            public void cancelled() {

            }
        }, false, false);
    }

}
