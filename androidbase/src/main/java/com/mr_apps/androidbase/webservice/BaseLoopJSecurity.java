package com.mr_apps.androidbase.webservice;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mr_apps.androidbase.preferences.SecurityPreferences;
import com.mr_apps.androidbase.utils.Logger;
import com.mr_apps.androidbase.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

/**
 * Created by denis on 17/06/16.
 * if you are not able to use ion (RECOMMENDED)
 */
public abstract class BaseLoopJSecurity extends WebServiceUtils{

    public enum Method{
        GET,
        POST
    }

    /**
     * @param context
     * @param url
     * @param path
     * @param params
     * @param complete
     * @param isSecurityEnabled
     * @param handleErrorCode
     * @return
     */
    public ResponseHandlerInterface getResponseHandlerInterface(final Context context, final String url, final String path, final RequestParams params, final FutureLoopJCallback<JsonElement> complete, final boolean isSecurityEnabled, final boolean handleErrorCode, final Method method) {

        return new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logger.d(TAG, "url: "+url
                        +(headers!=null?"\nheaders: "+ Arrays.toString(headers):""
                        +("responseString: "+responseString)));
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Logger.d(TAG, "url: "+url
                        +(headers!=null?"\nheaders: "+ Arrays.toString(headers):""
                        +("responseString: "+responseString)));

                Gson gson=new GsonBuilder().create();

                try {

                    JsonElement el = gson.fromJson(responseString, JsonElement.class);

                    if (el.isJsonObject()) {

                        JsonObject obj=el.getAsJsonObject();

                        if (!handleErrorCode) {
                            complete.onCompletedJsonObject(obj);
                            return;
                        }

                        if (handleErrorCode(context, obj)) {
                            updateSecurity(context);
                            baseOperationWithPath(context, path, params, complete, isSecurityEnabled, true, method);
                        } else
                            complete.onCompletedJsonObject(obj);

                    } else if (el.isJsonArray()) {
                        complete.onCompletedJsonArray(el.getAsJsonArray());
                    } else if (el.isJsonPrimitive()) {
                        complete.onCompletedJsonElement(el);
                    } else {
                        complete.onCompleted(null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    complete.onCompleted(null);
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
    public LoopJResult baseOperationWithPath(final Context context, final String path, final RequestParams params, final FutureLoopJCallback<JsonElement> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {

        return baseOperationWithPath(context, path, params, complete, isSecurityEnabled, handleErrorCode, Method.POST);

    }

    public LoopJResult baseOperationWithPath(final Context context, final String path, final RequestParams params, final FutureLoopJCallback<JsonElement> complete, final boolean isSecurityEnabled, final boolean handleErrorCode, Method method) {

        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();

        asyncHttpClient.removeAllHeaders();

        if (!Utils.isOnline(context)) {
            complete.onCompleted(null);

            return null;
        }

        //String language = Locale.getDefault().getLanguage();

        final String url = composeUrl(path);

        if (isSecurityEnabled) {
            Map<String, String> security = getSecurity(context);

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params + security.toString());

            asyncHttpClient.addHeader(WsseToken.HEADER_AUTHORIZATION, security.get(WsseToken.HEADER_AUTHORIZATION));
            asyncHttpClient.addHeader(WsseToken.HEADER_WSSE, security.get(WsseToken.HEADER_WSSE));

        } else {
            SecurityPreferences.setHeader(context, "");

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params);

        }

        RequestHandle requestHandle;

        if(method==Method.POST)
            requestHandle=asyncHttpClient.post(context, url, params, getResponseHandlerInterface(context, url, path, params, complete, isSecurityEnabled, handleErrorCode, method)).setTag(path);
        else
            requestHandle=asyncHttpClient.get(context, url, params, getResponseHandlerInterface(context, url, path, params, complete, isSecurityEnabled, handleErrorCode, method)).setTag(path);


        return new LoopJResult(asyncHttpClient, requestHandle);

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
    public LoopJResult baseOperationWithPathGet(final Context context, final String path, final RequestParams params, final FutureLoopJCallback<JsonElement> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {
        return baseOperationWithPath(context, path, params, complete, isSecurityEnabled, handleErrorCode, Method.GET);
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
