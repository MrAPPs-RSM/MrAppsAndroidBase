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
public abstract class WebServiceSecurity extends WebServiceUtils {

    private static final String TAG = "WebServiceSecurity";

    private static String baseUrl = "http://beta.json-generator.com/api/json/get/";

    /*
    * da settare all'oncreate dell applicazione
    */
    public static void setBaseUrl(String baseUrl) {
        WebServiceSecurity.baseUrl = baseUrl;
    }

    public static ResponseFuture secureOperationWithPath(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled) {

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

                if (handleErrorCode(context, result)) {
                    updateSecurity(context);
                    secureOperationWithPath(context, path, params, complete, isSecurityEnabled);
                } else
                    complete.onCompleted(e, result);

            }
        });

        return responseFuture;

    }

    public static ResponseFuture secureOperationWithPathGet(final Context context, final String path, final HashMap<String, List<String>> params, final FutureCallback<JsonObject> complete, final boolean isSecurityEnabled) {

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

                if (handleErrorCode(context, result)) {
                    updateSecurity(context);
                    secureOperationWithPath(context, path, params, complete, isSecurityEnabled);
                } else
                    complete.onCompleted(e, result);

            }
        });

        return responseFuture;

    }

    private static boolean handleErrorCode(final Context context, JsonObject result) {

        boolean error = false;
        int error_code = 0;

        if (result != null) {
            Logger.d(TAG, "risposta " + result.toString());

            error_code = putJsonInt(result.get("error_code"));

            error = error_code == 403;
        }

        if (error_code == 1006)//utente inesistente
        {
            SecurityPreferences.removeAll(context, SecurityPreferences.namePreferences);

            //context.startActivity(new Intent(context, SplashScreen.class));
            //((Activity) context).finish();
        } else if (error_code == 1002) {
            /*new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.ERRORE_ORARIO))
                    .setMessage(context.getString(R.string.ERRORE_ORARIO_MESSAGE))
                    .setNegativeButton(context.getString(R.string.ANNULLA), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(context.getString(R.string.IMPOSTAZIONI), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));

                        }
                    }).show();*/
        }

        return error;

    }


    public static Map<String, List<String>> getSecurity(Context context) {

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

    private static long diffInMinutes(long date1, long date2) {
        long diffInMillisec = date1 - date2;

        return TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
    }

    private static void updateSecurity(Context context) {
        WsseToken wsseToken = new WsseToken(context);
        SharedPreferences preferences = context.getSharedPreferences(SecurityPreferences.namePreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SecurityPreferences.authorization, wsseToken.getAuthorizationHeader());
        editor.putLong(SecurityPreferences.header_date, (new Date()).getTime());
        editor.putString(SecurityPreferences.header, wsseToken.getWsseHeader());
        editor.commit();
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
