package com.mr_apps.androidbase.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.mr_apps.androidbase.custom_views.WarningTextInputLayout;
import com.mr_apps.androidbasecore.R;

/**
 * Class that provides generic utils
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public class Utils {

    /**
     * Calculates the distance between two positions
     *
     * @param latitude1  latitude of the first position
     * @param longitude1 longitude of the first position
     * @param latitude2  latitude of the second position
     * @param longitude2 longitude of the second position
     * @return the distance in meters between the given positions
     */
    public static float distanceInMeters(double latitude1, double longitude1, double latitude2, double longitude2) {
        Location location1 = new Location("");
        location1.setLatitude(latitude1);
        location1.setLongitude(longitude1);

        Location location2 = new Location("");
        location2.setLatitude(latitude2);
        location2.setLongitude(longitude2);

        return location1.distanceTo(location2);
    }

    /**
     * Useful method to check if a trimmed string is empty or null
     *
     * @param string the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Redefinition of the String "contains" method, non case-sensitive
     *
     * @param s1 the first string
     * @param s2 the second string
     * @return true if the first string contains the second string (non case-sensitive), false otherwise
     */
    public static boolean containsIgnoreCase(String s1, String s2) {
        return s1.toLowerCase().contains(s2.toLowerCase());
    }

    /**
     * Redefinition of the String "substring" method
     *
     * @param string the string that has to be cut
     * @param end    the index of the end of the substring
     * @return the initial string cut until the end index, or the initial string if the index is higher than the string's size
     */
    public static String subString(String string, int end) {
        if (end > string.length())
            return string;
        else
            return string.substring(0, end);
    }

    /**
     * Ridefinition of the "isNullOrEmpty" method
     *
     * @param string the string that has to be check
     * @return true if the string is null or empty or is composed by the text "null", false otherwise
     */
    public static boolean isNullOrEmptyNoNull(String string) {
        return string == null || string.trim().isEmpty() || string.equalsIgnoreCase("NULL");
    }

    /**
     * Utility method that converts a float dp value to a pixel value
     *
     * @param dp      the dp value to convert
     * @param context the context
     * @return the dp value in pixel
     */
    public static int dpToPx(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Verifies if the device is connected to the internet
     *
     * @param context the context
     * @return true if the device is connected, false otherwise
     */
    public static boolean isOnline(Context context) {
        //inizializzo il connectivity manager
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //prendo informazioni sulle connessioni attive
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //se ho informazioni e  sono connesso o in attesa di connessione
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        //else di default, non sono connesso
        //Toast.makeText(context.getApplicationContext(), context.getString(R.string.offline), Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * Converts number of bytes into proper scale.
     *
     * @param bytes number of bytes to be converted.
     * @return A string that represents the bytes in a proper scale.
     */
    public static String getBytesString(long bytes) {
        String[] quantifiers = new String[]{
                "KB", "MB", "GB", "TB"
        };
        double speedNum = bytes;
        for (int i = 0; ; i++) {
            if (i >= quantifiers.length) {
                return "";
            }
            speedNum /= 1024;
            if (speedNum < 512) {
                return String.format("%.2f", speedNum) + " " + quantifiers[i];
            }
        }
    }

    /**
     * Utility method that checks if a email is valid or not
     *
     * @param target the email to check
     * @return true if the email is valid, false otherwise
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * Utility method that converts a int dp value to a pixel value
     *
     * @param context the context
     * @param dp      the dp value to convert
     * @return the dp value in pixel
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void passwordToggleDrawableColor(Context context, WarningTextInputLayout warningTextInputLayout) {
        if (warningTextInputLayout.isErrorEnabled()) {
            warningTextInputLayout.setPasswordVisibilityToggleTintList(new ColorStateList(new int[][]{
                    new int[]{android.R.attr.state_enabled},
            },
                    new int[]{
                            ContextCompat.getColor(context, R.color.errorRed),
                    }
            ));
        } else {
            warningTextInputLayout.setPasswordVisibilityToggleTintList(new ColorStateList(new int[][]{
                    new int[]{android.R.attr.state_enabled},
            },
                    new int[]{
                            Color.GRAY,
                    }
            ));
        }
    }
}
