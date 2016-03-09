package com.mr_apps.androidbase.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by denis on 19/01/2016.
 *
 * @author Denis Brandi
 */
public class GlobalShare {

    //public static final String fb_id="420026631431043";

    public static void share(Context context, String text, Bitmap bitmap) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }

        if (bitmap != null) {
            shareIntent.setType("image/*");
            //shareIntent.putExtra(Intent.EXTRA_STREAM, Bitmap.createScaledBitmap(bitmap, 640, 480, false));
            Bitmap bitmap1 = MaskedBitmap.makeItSquare(bitmap.getWidth(), bitmap, MaskedBitmap.SquareMode.LETTERBOX);

            String pathofBmp2 = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap1, "allegato", null);

            if (pathofBmp2 == null)
                return;

            Uri bmpUri = Uri.parse(pathofBmp2);

            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        }

// Include your Facebook App Id for attribution
        //shareIntent.putExtra("com.facebook.platform.extra.APPLICATION_ID", Settings.getFacebook_id(context));

        context.startActivity(Intent.createChooser(shareIntent, null));
    }

}
