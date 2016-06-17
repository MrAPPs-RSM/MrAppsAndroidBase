package com.mr_apps.androidbase.webservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;

/**
 * Created by denis on 17/06/16.
 */
public class LoopJResult {

    public AsyncHttpClient client;
    public RequestHandle requestHandle;

    public LoopJResult(AsyncHttpClient client, RequestHandle requestHandle) {
        this.client=client;
        this.requestHandle=requestHandle;
    }

}
