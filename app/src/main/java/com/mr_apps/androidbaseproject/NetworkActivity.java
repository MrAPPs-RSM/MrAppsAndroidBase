package com.mr_apps.androidbaseproject;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.loopj.android.http.RequestParams;
import com.mr_apps.androidbase.activity.AbstractBaseActivity;
import com.mr_apps.androidbase.webservice.BaseLoopJSecurity;

import cz.msebera.android.httpclient.concurrent.FutureCallback;

/**
 * Created by denis on 17/06/16.
 */
public class NetworkActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        setBackButton();

        final EditText baseurl=(EditText) findViewById(R.id.url);
        final EditText path=(EditText) findViewById(R.id.path);
        final TextView result=(TextView) findViewById(R.id.result);

        Button send=(Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseLoopJSecurity.setBaseUrl(baseurl.getText().toString());

                BaseLoopJSecurity instance=BaseLoopJSecurity.getInstance(new BaseLoopJSecurity() {
                    @Override
                    public boolean handleErrorCode(Context context, JsonObject result) {
                        return super.handleErrorCode(context, result);
                    }
                });

                instance.baseOperationWithPath(NetworkActivity.this, path.getText().toString(), (RequestParams) null, new FutureCallback<JsonObject>(){

                    @Override
                    public void completed(JsonObject object) {
                        result.setText(object.toString());
                    }

                    @Override
                    public void failed(Exception ex) {

                    }

                    @Override
                    public void cancelled() {

                    }

                }, false, false);

            }
        });

    }
}
