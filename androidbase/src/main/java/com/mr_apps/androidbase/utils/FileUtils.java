package com.mr_apps.androidbase.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by denis on 29/02/16
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    private static int durationAudio = 120000;
    private static int durationVideo = 30000;

    public enum ElementType {
        text,
        img,
        vid,
        audio
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static File newFileToUpload(Context context, String folder, ElementType type, boolean internal) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss__dd_MM_yy", Locale.getDefault());

        Date date = new Date();
        final String data = UUID.randomUUID() + "_" + sdf.format(date);

        File dir = getTempImageFolder(context, folder, internal);

        if (!dir.exists())
            dir.mkdirs();

        if (type == ElementType.img)
            return new File(dir.getPath(), data + "_image.jpg");
        else if (type == ElementType.vid)
            return new File(dir.getPath(), data + "_video.mp4");
        else if (type == ElementType.audio)
            return new File(dir.getPath(), data + "_audio.aac");
        else
            return null;

    }

    public static File getTempImageFolder(Context context, String folder, boolean internal) {
        String path = (internal ? getFilePath(context, folder).getPath() : getExternalPath(folder));

        return new File(path);

    }

    public static File getFilePath(Context context, String folder) {
        String filePath = context.getFilesDir().getPath() + "/" + folder;
        File directory = new File(filePath);
        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        return new File(filePath);
    }

    public static String getExternalPath(String folder) {
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + folder;

        File directory = new File(filePath);

        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        return filePath;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

    /*public static String getVideoPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        System.out.println("path: " + filePath);

        return filePath;
    }*/

    public static File generateFileFromBitmap(Context context, String folder, Bitmap photo, boolean internal, int quality) {
        if (photo != null) {
            ByteArrayOutputStream bytes = BitmapUtils.reduceUntilRespectSize(photo, 512000, quality);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH_mm_ss__dd_MM_yyyy", Locale.getDefault());

            Date date = new Date();

            String datas = simpleDateFormat.format(date);
            String newimagename = UUID.randomUUID().toString() + "_" + datas + "_image.jpg";

            File f = new File(internal ? getFilePath(context, folder).getPath(): getExternalPath(folder),  newimagename);

            return writeBytes(f, bytes);

        } else {
            Toast.makeText(context, "Devi selezionare un file che sia presente in memoria", Toast.LENGTH_SHORT).show();

            return null;
        }

    }

    /**
     * Genera un file da un'immagine bitmap e lo salva nella cartella scelta usando il nome passato come parametro
     * @param context Context
     * @param folder La cartella all'interno della quale deve essere salvato file
     * @param photo Il file bitmap da cui generare il file
     * @param fileName Il nome del file
     * @return il file generato, o null se era già presente nella folder un file con lo stesso nome
     */
    public static File generateFileFromBitmap(Context context, String folder, Bitmap photo, String fileName, int quality) {
        if (photo != null) {

            ByteArrayOutputStream bytes = BitmapUtils.reduceUntilRespectSize(photo, 512000, quality);
            File f = new File(getExternalPath(folder.endsWith("/") ? folder : folder + "/") + fileName);

            return writeBytes(f, bytes);

        } else {
            Toast.makeText(context, "Devi selezionare un file che sia presente in memoria", Toast.LENGTH_SHORT).show();

            return null;
        }
    }

    private static File writeBytes(File f, ByteArrayOutputStream bytes) {

        try {
            if (!f.createNewFile())
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fo = null;

        try {
            fo = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long size = f.length();
        Logger.d(TAG, String.format("size of bitmap: %d", size));

        return f;
    }

    /**
     * reduces the size of the image
     *
     * @param f
     * @param maxSize
     * @return
     */
    public static File getResizedImageFile(Context context, String folder, File f, int maxSize, boolean internal, int quality) throws FileNotFoundException {

        BitmapFactory.Options thumbOpts = new BitmapFactory.Options();
        thumbOpts.inSampleSize = 4;

        Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f), null, thumbOpts);

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(image, width, height, true);

        File file = generateFileFromBitmap(context, folder, bitmap, internal, quality);

        return file;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static class MediaSelected {
        public String type;
        public String path;

        public MediaSelected(String type, String path) {
            this.type = type;
            this.path = path;
        }
    }

    public static boolean checkAudioDuration(Context context, Uri filmedVideo) {

        MediaPlayer mediaPlayer = MediaPlayer.create(context, filmedVideo);

        int duration = mediaPlayer.getDuration();

        mediaPlayer.release();

        if (duration > durationAudio) {
            Snackbar.make(((Activity) context).getCurrentFocus(), "Il file è troppo grande", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean checkVideoDuration(Context context, Uri filmedVideo) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, filmedVideo);

        int duration = mediaPlayer.getDuration();

        mediaPlayer.release();

        if (duration > durationVideo) {
            Snackbar.make(((Activity) context).getCurrentFocus(), "Il file è troppo grande", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static MediaSelected getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        String elementType = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return new MediaSelected(elementType, Environment.getExternalStorageDirectory() + "/" + split[1]);
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    elementType = "img";
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return new MediaSelected(elementType, cursor.getString(column_index));
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return new MediaSelected(elementType, uri.getPath());
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        String realPath = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                realPath = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        if (realPath == null)
            Toast.makeText(context, "impossibile accedere al file", Toast.LENGTH_SHORT).show();

        return realPath;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
