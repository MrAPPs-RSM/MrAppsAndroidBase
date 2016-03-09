package com.mr_apps.androidbase.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.List;

/**
 * Created by denis on 19/01/2016.
 *
 * @author Denis Brandi
 */
public class Sharing {

    public static final String gmail="com.google.android.gm";
    public static final String gplus="com.google.android.apps.plus";
    public static final String instagram="com.instagram.android";
    public static final String whatsapp="com.whatsapp";
    public static final String messaggio="";
    public static final String facebook="com.facebook.katana";
    public static final String twitter="com.twitter.android";
    public static final String youtube="com.google.android.youtube";

    /*public static void shareFacebook(Context context, String text, String url, String image)
    {
        Intent intent= new Intent(context, FacebookWindow.class);
        intent.putExtra("testo", text);
        intent.putExtra("url", url);
        intent.putExtra("image", image);
        context.startActivity(intent);
    }*/

    public static void shareMessage(Context context, String text)
    {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", text);
        context.startActivity(sendIntent);

    }

    public static void shareGmail(Context context, String text, Uri bitmap)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        if(bitmap!=null)
            sendIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(gmail);
        context.startActivity(sendIntent);
    }

    public static void shareGplus(Context context, String text, Uri bitmap)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        if(bitmap!=null)
            sendIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(gplus);
        context.startActivity(sendIntent);
    }

    public static void shareInstagram(Context context, String text, Uri bitmap, Bitmap bitmapToResize)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        if(bitmap!=null) {

            Bitmap bitmap1=MaskedBitmap.makeItSquare(bitmapToResize.getWidth(), bitmapToResize, MaskedBitmap.SquareMode.LETTERBOX);

            String pathofBmp = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap1, "allegato", null);
            Uri bmpUri= Uri.parse(pathofBmp);

            sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        }
        sendIntent.setType("image/*");
        sendIntent.setPackage(instagram);
        context.startActivity(sendIntent);
    }

    public static void shareWhatsapp(Context context, String text)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(whatsapp);
        context.startActivity(sendIntent);

    }

    public static void shareTwitter(Context context, String text, Uri bitmap)
    {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.setType("text/plain");//tweetIntent.setType("application/twitter");
        tweetIntent.putExtra(Intent.EXTRA_TEXT, text);
        if(bitmap!=null)
            tweetIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
        tweetIntent.setPackage(twitter);
        /*
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> lract
                = pm.queryIntentActivities(tweetIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;

        for(ResolveInfo ri: lract)
        {
            if(ri.activityInfo.name.endsWith(".SendTweet"))
            {
                tweetIntent.setClassName(ri.activityInfo.packageName,
                        ri.activityInfo.name);
                resolved = true;
                break;
            }
        }

        if(resolved) {*/
        context.startActivity(tweetIntent);
        //}

    }


    public static boolean isPackageExisted(Context context, String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public static Drawable getIcon(Context context, String targetPackage)
    {
        Drawable icon=null;
        try {
            icon=context.getPackageManager().getApplicationIcon(targetPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }



}
