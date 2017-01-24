package com.mr_apps.androidbase.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mr_apps.androidbase.preferences.SecurityPreferences;
import com.mr_apps.androidbaseutils.utils.Logger;
import com.mr_apps.androidbaseutils.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.channels.NotYetConnectedException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.concurrent.FutureCallback;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by denis on 17/06/16.
 * if you are not able to use ion (RECOMMENDED)
 */
public abstract class BaseLoopJSecurity extends WebServiceUtils {

    private static AsyncHttpClient asyncHttpClient;

    public static AsyncHttpClient getAsyncHttpClient() {

        if (asyncHttpClient == null)
            asyncHttpClient = new AsyncHttpClient();

        return asyncHttpClient;
    }

    public static void trustAllCertificates() {
        getAsyncHttpClient().setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
    }

    public enum Method {
        GET,
        POST
    }

    /**
     * @param context
     * @param url
     * @param path
     * @param params
     * @param completeObject
     * @param completeArray
     * @param isSecurityEnabled
     * @param handleErrorCode
     * @return
     */
    public ResponseHandlerInterface getResponseHandlerInterface(final Context context, final String url, final String path, final RequestParams params, final String json, final FutureCallback<JsonObject> completeObject, final FutureCallback<JsonArray> completeArray, final boolean isSecurityEnabled, final boolean handleErrorCode, final Method method) {

        return new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Logger.d(TAG, "call to: " + url);
                if (params != null)
                    Logger.d(TAG, "with params: " + params.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logger.d(TAG, "url: " + url
                        + (headers != null ? "\nheaders: " + Arrays.toString(headers) : ""
                        + ("\nresponseString: " + responseString)));
                throwable.printStackTrace();
                if (completeObject != null)
                    completeObject.failed(new Exception());
                if (completeArray != null)
                    completeArray.failed(new Exception());

            }

            @Override
            public void onCancel() {
                if (completeObject != null)
                    completeObject.cancelled();
                if (completeArray != null)
                    completeArray.cancelled();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (responseString == null) {
                    if (completeObject != null)
                        completeObject.failed(null);
                    if (completeArray != null)
                        completeArray.failed(null);
                }

                Gson gson = new GsonBuilder().create();

                try {

                    Logger.d(TAG, "response: " + responseString);

                    JsonElement el = gson.fromJson(responseString, JsonElement.class);

                    if (el.isJsonObject()) {

                        JsonObject obj = el.getAsJsonObject();

                        if (!handleErrorCode) {
                            completeObject.completed(obj);
                            return;
                        }

                        if (handleErrorCode(context, obj)) {
                            updateSecurity(context);
                            baseOperationWithPath(context, path, params, json, completeObject, completeArray, isSecurityEnabled, true, method);
                        } else
                            completeObject.completed(obj);

                    } else if (el.isJsonArray()) {

                        JsonArray array = el.getAsJsonArray();

                        completeArray.completed(array);

                    } else {
                        completeArray.failed(new JsonParseException("not a json object"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    completeObject.failed(e);
                }


            }
        };

    }

    private static final String TAG = "BaseLoopJSecurity";

    private static String baseUrl = "http://beta.json-generator.com/api/json/get/";

    public static final int GENERIC_ERROR = 1001;
    public static final int CREATEDAT_TOO_NEXT = 1002;
    public static final int CREATEDAT_TOO_PREVIOUS = 1003;
    public static final int DIGEST_ALREADY_IN_USE = 1004;
    public static final int WRONG_DIGEST = 1005;
    public static final int USER_DOES_NOT_EXIST = 1006;

    private static BaseLoopJSecurity instance;

    /**
     * Gets the instance of this class
     *
     * @param implementation the implementation of the BaseWebServiceSecurity
     * @return the instance
     */
    public static BaseLoopJSecurity getInstance(BaseLoopJSecurity implementation) {

        if (instance == null)
            instance = implementation;

        return instance;
    }

    /**
     * Sets the base Url of the app, that is used for every web service call
     *
     * @param baseUrl the base url of the app
     */
    public static void setBaseUrl(String baseUrl) {
        BaseLoopJSecurity.baseUrl = baseUrl;
    }

    /**
     * Calls the given URL with all the additional strings passed as parameters
     *
     * @param context           the context
     * @param path              the path of the url to call
     * @param params            a map containing all the additional parameters of the call
     * @param complete          callback for the completion of the call
     * @param isSecurityEnabled true if the WSSE security is enabled for this call, false otherwise
     * @param handleErrorCode   parameter for the error's code management
     * @return the response of the web service
     */
    public HttpResult baseOperationWithPath(final Context context, final String path, final RequestParams params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, params, null, complete, null, isSecurityEnabled, handleErrorCode, Method.POST);
    }

    public HttpResult baseOperationWithPath(final Context context, final String path, final String json, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, null, json, complete, null, isSecurityEnabled, handleErrorCode, Method.POST);
    }

    public HttpResult baseOperationWithPath(final Context context, final String path, final RequestParams params, final FutureCallback<JsonObject> completeObject, final FutureCallback<JsonArray> completeArray, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, params, null, completeObject, completeArray, isSecurityEnabled, handleErrorCode, Method.POST);
    }

    public HttpResult baseOperationWithPath(final Context context, final String path, final String json, final FutureCallback<JsonObject> completeObject, final FutureCallback<JsonArray> completeArray, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, null, json, completeObject, completeArray, isSecurityEnabled, handleErrorCode, Method.POST);
    }

    public HttpResult baseOperationWithPath(final Context context, final String path, final RequestParams params, String json, final FutureCallback<JsonObject> completeObject, final FutureCallback<JsonArray> completeArray, final boolean isSecurityEnabled, final boolean handleErrorCode, Method method) {

        AsyncHttpClient asyncHttpClient = getAsyncHttpClient();

        asyncHttpClient.removeAllHeaders();

        if (!Utils.isOnline(context)) {
            if (completeObject != null)
                completeObject.failed(new NotYetConnectedException());
            if (completeArray != null)
                completeArray.failed(new NotYetConnectedException());

            return null;
        }

        //String language = Locale.getDefault().getLanguage();

        final String url = composeUrl(path);

        if (isSecurityEnabled) {
            Map<String, String> security = getSecurity(context);

            asyncHttpClient.addHeader(WsseToken.HEADER_AUTHORIZATION, security.get(WsseToken.HEADER_AUTHORIZATION));
            asyncHttpClient.addHeader(WsseToken.HEADER_WSSE, security.get(WsseToken.HEADER_WSSE));

        } else {
            SecurityPreferences.setHeader(context, "");

        }

        RequestHandle requestHandle = null;

        if (method == Method.POST) {
            if (params != null)
                requestHandle = asyncHttpClient.post(context, url, params, getResponseHandlerInterface(context, url, path, params, json, completeObject, completeArray, isSecurityEnabled, handleErrorCode, method)).setTag(path);
            else if(json!=null){

                StringEntity stringEntity = null;
                try {
                    stringEntity = new StringEntity(json);
                    requestHandle = asyncHttpClient.post(context, url, stringEntity, "application/json", getResponseHandlerInterface(context, url, path, params, json, completeObject, completeArray, isSecurityEnabled, handleErrorCode, method)).setTag(path);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        } else {
            if (params != null)
                requestHandle = asyncHttpClient.get(context, url, params, getResponseHandlerInterface(context, url, path, params, json, completeObject, completeArray, isSecurityEnabled, handleErrorCode, method)).setTag(path);
            else if(json!=null){
                StringEntity stringEntity = null;
                try {
                    stringEntity = new StringEntity(json);
                    requestHandle = asyncHttpClient.get(context, url, stringEntity, "application/json", getResponseHandlerInterface(context, url, path, params, json, completeObject, completeArray, isSecurityEnabled, handleErrorCode, method)).setTag(path);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return new HttpResult(asyncHttpClient, requestHandle);

    }

    /**
     * Calls the given URL with all the additional strings passed as parameters, using the GET method
     *
     * @param context           the context
     * @param path              the path of the url to call
     * @param params            a map containing all the additional parameters of the call
     * @param complete          callback for the completion of the call
     * @param isSecurityEnabled true if the WSSE security is enabled for this call, false otherwise
     * @param handleErrorCode   parameter for the error's code management
     * @return the response of the web service
     */
    public HttpResult baseOperationWithPathGet(final Context context, final String path, final RequestParams params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, params, null, complete, null, isSecurityEnabled, handleErrorCode, Method.GET);
    }

    public HttpResult baseOperationWithPathGet(final Context context, final String path, final RequestParams params, final FutureCallback<JsonObject> completeObject, final FutureCallback<JsonArray> completeArray, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, params, null, completeObject, completeArray, isSecurityEnabled, handleErrorCode, Method.GET);
    }

    /**
     * Method to override to implement the security handler for the error code
     *
     * @param context the context
     * @param result  the result of the call
     * @return false by default
     */
    public boolean handleErrorCode(final Context context, JsonObject result) {
        return false;
    }

    /**
     * Update the Security Preferences
     *
     * @param context the context
     */
    public void updateSecurity(Context context) {
        WsseToken wsseToken = new WsseToken(context);
        SecurityPreferences.setAuthorization(context, wsseToken.getAuthorizationHeader());
        SecurityPreferences.setHeader_date(context, (new Date()).getTime());
        SecurityPreferences.setHeader(context, wsseToken.getWsseHeader());
        /*SharedPreferences preferences = context.getSharedPreferences(SecurityPreferences.namePreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SecurityPreferences.authorization, wsseToken.getAuthorizationHeader());
        editor.putLong(SecurityPreferences.header_date, (new Date()).getTime());
        editor.putString(SecurityPreferences.header, wsseToken.getWsseHeader());
        editor.commit();*/
    }

    /**
     * Gets the security map
     *
     * @param context the context
     * @return the security map for this class
     */
    public Map<String, String> getSecurity(Context context) {

        long diffInMinutes = diffInMinutes((new Date()).getTime(), SecurityPreferences.getHeader_date(context));

        String header = SecurityPreferences.getHeader(context);

        if (Utils.isNullOrEmpty(header) || diffInMinutes > 4) {
            updateSecurity(context);
        }

        Map<String, String> map = new HashMap<>();

        map.put(WsseToken.HEADER_AUTHORIZATION, SecurityPreferences.getAuthorization(context));
        map.put(WsseToken.HEADER_WSSE, SecurityPreferences.getHeader(context));

        return map;
    }

    /**
     * Calculates the difference in minutes between two dates
     *
     * @param date1 first date
     * @param date2 second date
     * @return the difference in minutes between the two dates
     */
    public static long diffInMinutes(long date1, long date2) {
        long diffInMillisec = date1 - date2;

        return TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
    }

    /**
     * Composes an URL using the base Url
     *
     * @param path the name of the API to compose the URL to call
     * @return the URL of the API composed by the base Url and the path passed by parameter
     */
    public static String composeUrl(String path) {
        return baseUrl + path;//String.format("/%s", "api") + String.format("/%s", VERSION)+String.format("/%s", path);
    }

    /**
     * The MD5 encoding algorithm
     *
     * @param md5 the string to encode
     * @return the string encoded using the MD5 algorithm
     */
    public static String MD5(String md5) {

        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }

            //Log.d("token", sb.toString());

            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
