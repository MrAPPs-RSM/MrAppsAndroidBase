package com.mr_apps.androidbase.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Class that contains basic Bitmap Utils
 *
 * @author Denis Brandi
 * @author Mattia Ruggiero
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    /**
     * Scales the given bitmap to a max dimension of 1280x720
     *
     * @param bm the bitmap to be scaled
     * @return the scaled bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bm) {
        return scaleBitmap(bm, 1280, 720);
    }

    /**
     * Scales the given bitmap at the given max width and max height, mainteining the aspect ratio
     *
     * @param bm        the bitmap to be scaled
     * @param maxWidth  the max width of the final bitmap
     * @param maxHeight the max height of the final bitmap
     * @return the scaled bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bm, float maxWidth, float maxHeight) {
        float width = bm.getWidth();
        float height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxWidth;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, (int) width, (int) height, true);
        return bm;
    }

    /**
     * Rotates the given bitmap to the correct rotation, using the given orientation
     *
     * @param bitmap      the bitmap to be rotated
     * @param orientation the orientation
     * @return the rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        try {
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }
            try {
                Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //return bitmap;
    }

    /**
     * Reduces the size of the given bitmap
     *
     * @param bitmap  the bitmap to be reduced
     * @param size    the size of the new bitmap
     * @param quality the quality of the compression of the bitmap, from 1 (low quality) to 100 (high quality)
     * @return the byte stream of the reduced bitmap
     */
    public static ByteArrayOutputStream reduceUntilRespectSize(Bitmap bitmap, int size, int quality) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, o);

        int byteCount = o.toByteArray().length;//BitmapCompat.getAllocationByteCount(bitmap);

        if (byteCount > size) {

            o = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, o);

            Logger.d(TAG, "compresso a " + String.valueOf(o.toByteArray().length));

        }

        return o;
    }

    /**
     * Method that contains a procedure to be used for big images
     *
     * @param context       the context
     * @param selectedImage the Uri of the big image
     * @return a bitmap from the given Uri
     */
    public static Bitmap procedureBigImage(Context context, Uri selectedImage) {
        if (selectedImage != null) {

            try {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                return BitmapFactory.decodeFile(picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    /**
     * Gets a bitmap from the given file, if possible
     *
     * @param image the file that has to be converted to a bitmap
     * @return the bitmap generated from the file, or null if the file is not convertible to a bitmap
     */
    public static Bitmap getBitmapFromFile(File image) {

        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Method that contains a procedure to be used for standard images
     *
     * @param context       the context
     * @param selectedImage the Uri of the image
     * @return a bitmap from the given Uri
     */
    public static Bitmap procedureImage(Context context, Uri selectedImage) {
        if (selectedImage != null) {

            try {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                //Bitmap yourSelectedImage =
                BitmapFactory.decodeFile(picturePath, options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 720, 1280);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;

                return BitmapFactory.decodeFile(picturePath, options);//yourSelectedImage;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    /**
     * Calculate the "inSampleSize" value of a bitmap, using the given width and height
     *
     * @param options   the options of the bitmap
     * @param reqWidth  pixels required for the width of image
     * @param reqHeight pixels required for the height of image
     * @return the "inSampleSize" value of the bitmap
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
