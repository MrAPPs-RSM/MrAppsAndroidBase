package com.mr_apps.androidbase.webservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;

/**
 * Created by denis on 17/06/16.
 */
public class HttpResult {

    public AsyncHttpClient client;
    public RequestHandle requestHandle;

    public HttpResult(AsyncHttpClient client, RequestHandle requestHandle) {
        this.client=client;
        this.requestHandle=requestHandle;
    }

    public void cancelRequest() {
        if(client!=null && requestHandle!=null) {
            client.cancelRequestsByTAG(requestHandle.getTag(), true);
        }
    }

}
