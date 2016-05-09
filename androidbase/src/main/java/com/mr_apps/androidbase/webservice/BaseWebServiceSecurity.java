package com.mr_apps.androidbase.webservice;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;
import com.mr_apps.androidbase.preferences.SecurityPreferences;
import com.mr_apps.androidbase.utils.Logger;
import com.mr_apps.androidbase.utils.Utils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages the base Web Service Security
 *
 * @author Denis Brandi
 */
public abstract class BaseWebServiceSecurity extends WebServiceUtils {

    private static final String TAG = "BaseWebServiceSecurity";

    private static String baseUrl = "http://beta.json-generator.com/api/json/get/";

    public static final int GENERIC_ERROR = 1001;
    public static final int CREATEDAT_TOO_NEXT = 1002;
    public static final int CREATEDAT_TOO_PREVIOUS = 1003;
    public static final int DIGEST_ALREADY_IN_USE = 1004;
    public static final int WRONG_DIGEST = 1005;
    public static final int USER_DOES_NOT_EXIST = 1006;

    private static BaseWebServiceSecurity instance;

    /**
     * Gets the instance of this class
     *
     * @param implementation the implementation of the BaseWebServiceSecurity
     * @return the instance
     */
    public static BaseWebServiceSecurity getInstance(BaseWebServiceSecurity implementation) {

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
        BaseWebServiceSecurity.baseUrl = baseUrl;
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
    public ResponseFuture baseOperationWithPath(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {

        if (!Utils.isOnline(context)) {
            complete.onCompleted(null, null);

            return null;
        }

        //String language = Locale.getDefault().getLanguage();

        String url = composeUrl(path);

        Builders.Any.B builder = Ion.with(context)
                .load(url);

        if (isSecurityEnabled) {
            Map<String, List<String>> security = getSecurity(context);

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params.toString() + security.toString());

            builder.addHeaders(security);
        } else {
            SecurityPreferences.setHeader(context, "");

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params.toString());

        }

        ResponseFuture<JsonObject> responseFuture = builder
                .setBodyParameters(params)
                .asJsonObject();

        responseFuture.withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> jsonResponse) {

                if (jsonResponse == null) {
                    complete.onCompleted(e, null);
                    return;
                }

                Logger.d("esito: " + jsonResponse.getHeaders().message(), "chiamata: " + jsonResponse.getRequest().getUri().toString());

                JsonObject result = jsonResponse.getResult();

                if (result != null)
                    Logger.d(TAG, "risposta " + result.toString());

                if (!handleErrorCode) {
                    complete.onCompleted(e, result);
                    return;
                }

                if (handleErrorCode(context, result)) {
                    updateSecurity(context);
                    baseOperationWithPath(context, path, params, complete, isSecurityEnabled, true);
                } else
                    complete.onCompleted(e, result);

            }
        });

        return responseFuture;

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
    public ResponseFuture baseOperationWithPathGet(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {

        if (!Utils.isOnline(context)) {
            complete.onCompleted(null, null);

            return null;
        }


        String url = composeUrl(path);

        Builders.Any.B builder = Ion.with(context)
                .load(url);

        if (isSecurityEnabled) {
            Map<String, List<String>> security = getSecurity(context);

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params.toString() + security.toString());

            builder.addHeaders(security);
        } else {
            SecurityPreferences.setHeader(context, "");

            Logger.d(TAG, "chiamata a " + url + "\ncon parametri aggiuntivi " + params.toString());

        }

        ResponseFuture<JsonObject> responseFuture = builder
                .addQueries(params)
                .asJsonObject();

        responseFuture.withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> jsonResponse) {

                if (jsonResponse == null) {
                    complete.onCompleted(e, null);
                    return;
                }

                Logger.d("esito: " + jsonResponse.getHeaders().message(), "chiamata: " + jsonResponse.getRequest().getUri().toString());

                JsonObject result = jsonResponse.getResult();

                if (result != null)
                    Logger.d(TAG, "risposta " + result.toString());

                if (!handleErrorCode) {
                    complete.onCompleted(e, result);
                    return;
                }

                if (handleErrorCode(context, result)) {
                    updateSecurity(context);
                    baseOperationWithPathGet(context, path, params, complete, isSecurityEnabled, true);
                } else
                    complete.onCompleted(e, result);

            }
        });

        return responseFuture;

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
    public Map<String, List<String>> getSecurity(Context context) {

        long diffInMinutes = diffInMinutes((new Date()).getTime(), SecurityPreferences.getHeader_date(context));

        String header = SecurityPreferences.getHeader(context);

        if (Utils.isNullOrEmpty(header) || diffInMinutes > 4) {
            updateSecurity(context);
        }

        Map<String, List<String>> map = new HashMap<>();

        map.put(WsseToken.HEADER_AUTHORIZATION, Collections.singletonList(SecurityPreferences.getAuthorization(context)));
        map.put(WsseToken.HEADER_WSSE, Collections.singletonList(SecurityPreferences.getHeader(context)));

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
