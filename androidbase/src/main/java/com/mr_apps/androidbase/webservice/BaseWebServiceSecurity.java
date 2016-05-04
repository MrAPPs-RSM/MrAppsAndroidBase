package com.mr_apps.androidbase.webservice;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;
import com.mr_apps.androidbase.preferences.SecurityPreferences;
import com.mr_apps.androidbase.utils.Logger;
import com.mr_apps.androidbase.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by denis on 03/02/16
 */
public abstract class BaseWebServiceSecurity extends WebServiceUtils {

    private static final String TAG = "BaseWebServiceSecurity";

    private static String baseUrl = "http://beta.json-generator.com/api/json/get/";

    public static final int GENERIC_ERROR=1001;
    public static final int CREATEDAT_TOO_NEXT=1002;
    public static final int CREATEDAT_TOO_PREVIOUS=1003;
    public static final int DIGEST_ALREADY_IN_USE=1004;
    public static final int WRONG_DIGEST=1005;
    public static final int USER_DOES_NOT_EXIST=1006;

    private static BaseWebServiceSecurity instance;

    public static BaseWebServiceSecurity getInstance(BaseWebServiceSecurity implementation) {

        if(instance==null)
            instance=implementation;

        return instance;
    }

    /*
        * da settare all'oncreate dell applicazione
        */
    public static void setBaseUrl(String baseUrl) {
        BaseWebServiceSecurity.baseUrl = baseUrl;
    }

    public ResponseFuture baseOperationWithPath(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {

        if (!Utils.isOnline(context)) {
            complete.onCompleted(null, null);

            return null;
        }

        //String language = Locale.getDefault().getLanguage();

        String url = composeUrl(path);

        Builders.Any.B builder=Ion.with(context)
                .load(url);

        if(isSecurityEnabled) {
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

                if(result!=null)
                    Logger.d(TAG, "risposta " + result.toString());

                if(!handleErrorCode) {
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

    public ResponseFuture baseOperationWithPathGet(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled, final boolean handleErrorCode) {

        if (!Utils.isOnline(context)) {
            complete.onCompleted(null, null);

            return null;
        }


        String url = composeUrl(path);

        Builders.Any.B builder=Ion.with(context)
                .load(url);

        if(isSecurityEnabled) {
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

                if(result!=null)
                    Logger.d(TAG, "risposta " + result.toString());

                if(!handleErrorCode) {
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

    public boolean handleErrorCode(final Context context, JsonObject result) {
        return false;
    }

    public void updateSecurity(Context context) {
        WsseToken wsseToken = new WsseToken(context);
        SharedPreferences preferences = context.getSharedPreferences(SecurityPreferences.namePreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SecurityPreferences.authorization, wsseToken.getAuthorizationHeader());
        editor.putLong(SecurityPreferences.header_date, (new Date()).getTime());
        editor.putString(SecurityPreferences.header, wsseToken.getWsseHeader());
        editor.commit();
    }


    public Map<String, List<String>> getSecurity(Context context) {

        long diffInMinutes = diffInMinutes((new Date()).getTime(), SecurityPreferences.getHeader_date(context));

        String header=SecurityPreferences.getHeader(context);

        if (Utils.isNullOrEmpty(header) || diffInMinutes > 4) {
            updateSecurity(context);
        }

        Map<String, List<String>> map = new HashMap<>();

        map.put(WsseToken.HEADER_AUTHORIZATION, Arrays.asList(SecurityPreferences.getAuthorization(context)));
        map.put(WsseToken.HEADER_WSSE, Arrays.asList(SecurityPreferences.getHeader(context)));

        return map;
    }

    public static long diffInMinutes(long date1, long date2) {
        long diffInMillisec = date1 - date2;

        return TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
    }

    public static String composeUrl(String path) {
        return baseUrl + path;//String.format("/%s", "api") + String.format("/%s", VERSION)+String.format("/%s", path);
    }

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
