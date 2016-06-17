package com.mr_apps.androidbase.webservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by denis on 17/06/16.
 */
public abstract class FutureLoopJCallback<T> {

    public void onCompleted(T object){}

    public void onCompletedJsonArray(JsonArray array){}

    public void onCompletedJsonObject(JsonObject object){}

    public void onCompletedJsonElement(JsonElement element){}

}
