package com.mr_apps.androidbase.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.utils.BitmapUtils;
import com.mr_apps.androidbase.utils.FileUtils;
import com.mr_apps.androidbase.utils.FileUtils.ElementType;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by denis on 29/02/16
 */

/**
 * To avoid activity recreation remember to add "android:configChanges="orientation|keyboardHidden|screenSize"
 * in your manifest.
 */

public abstract class PickerActivity extends LocationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageUri = null;
        audioUri = null;
        videoUri = null;
    }

    private String mTitle;

    private Uri imageUri, audioUri, videoUri;

    private static final int actionPickImage = 1000;
    private static final int actionTakeImage = 2000;
    private static final int actionPickVideo = 3000;
    private static final int actionRecordVideo = 4000;
    private static final int actionPickAudio = 5000;
    private static final int actionRecordAudio = 6000;

    private int qualityImage = 80;

    public void setQualityImage(int qualityImage) {
        this.qualityImage = qualityImage;
    }

    private boolean saveInInternalStorage = false;

    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto) {
        showAlertChoice(title, pickImageFromGallery, takePhoto, false, false);
    }

    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto, boolean pickVideoFromGallery, boolean recordVideo) {
        showAlertChoice(title, pickImageFromGallery, takePhoto, pickVideoFromGallery, recordVideo, false, false);
    }

    public void showAlertChoice(String title, boolean pickImageFromGallery, final boolean takePhoto, boolean pickVideoFromGallery, boolean recordVideo, boolean pickAudioFromGallery, boolean recordAudio) {

        this.mTitle = title;

        imageUri = null;
        videoUri = null;

        if (!checkOrRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_storage))
            return;

        final String scegli_immagine = getString(R.string.Scegli_immagine_da_galleria);
        final String scatta_foto = getString(R.string.Scatta_foto);
        final String scegli_audio = getString(R.string.Scegli_audio_da_galleria);
        final String registra_audio = getString(R.string.Registra_audio);
        final String scegli_video = getString(R.string.Scegli_video_da_galleria);
        final String gira_video = getString(R.string.Gira_video);

        ArrayList<String> itemsArrayList = new ArrayList<>();

        if (pickImageFromGallery)
            itemsArrayList.add(scegli_immagine);

        if (takePhoto)
            itemsArrayList.add(scatta_foto);

        if (pickVideoFromGallery)
            itemsArrayList.add(scegli_video);

        if (recordVideo)
            itemsArrayList.add(gira_video);

        if (pickAudioFromGallery)
            itemsArrayList.add(scegli_audio);

        if (recordAudio)
            itemsArrayList.add(registra_audio);

        final String[] items = new String[itemsArrayList.size()];

        for (int i = 0; i < itemsArrayList.size(); i++)
            items[i] = itemsArrayList.get(i);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String choice = items[which];

                        if (choice.equals(scegli_immagine)) {

                            pickImage();

                        } else if (choice.equals(scatta_foto)) {

                            takePhoto();

                        } else if (choice.equals(scegli_video)) {

                            pickVideo();

                        } else if (choice.equals(gira_video)) {

                            recordVideo();

                        } else if (choice.equals(scegli_audio)) {

                            pickAudio();

                        } else if (choice.equals(registra_audio)) {

                            recordAudio();

                        }

                    }
                }).show();

    }

    public void pickImage() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        startActivityForResult(intent, actionPickImage);
    }

    public void takePhoto() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.img, saveInInternalStorage);
        imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, actionTakeImage);
    }

    public void pickVideo() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("video/*");
        startActivityForResult(intent, actionPickVideo);
    }

    public void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.vid, false);
        videoUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, getDurationVideo());
        startActivityForResult(intent, actionRecordVideo);
    }

    public void pickAudio() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("audio/*");
        startActivityForResult(intent, actionPickAudio);
    }

    public void recordAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.audio, false);
        audioUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, audioUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, getDurationAudio());
        startActivityForResult(intent, actionRecordAudio);
    }

    public int getDurationVideo() {
        return 30;
    }

    public int getDurationAudio() {
        return 120;
    }

    public String getFolder() {
        return "";
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setSaveInInternalStorage(boolean saveInInternalStorage) {
        this.saveInInternalStorage = saveInInternalStorage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path;
        ElementType type;

        switch (requestCode) {

            case actionTakeImage:
                handleTempUri(imageUri);
                break;
            case actionRecordVideo:
                handleVideoTempUri(videoUri);
                break;

            case actionRecordAudio:
                handleAudioTempUri(audioUri);
                break;

            case actionPickImage:

                if (data == null)
                    return;

                Uri uri = data.getData();

                try {

                    FileUtils.MediaSelected mediaSelected = FileUtils.getPath(this, uri);
                    path = mediaSelected.path;
                    type = ElementType.img;

                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    pickerResult(path, type, bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case actionPickVideo:

                if (data == null)
                    return;

                Uri uriVideo = data.getData();

                try {

                    FileUtils.MediaSelected mediaSelected = FileUtils.getPath(this, uriVideo);
                    path = mediaSelected.path;
                    type = ElementType.vid;

                    pickerResult(path, type, null);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case actionPickAudio:

                if (data == null)
                    return;

                Uri uriAudio = data.getData();

                try {

                    FileUtils.MediaSelected mediaSelected = FileUtils.getPath(this, uriAudio);
                    path = mediaSelected.path;
                    type = ElementType.audio;

                    pickerResult(path, type, null);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    public void pickerResult(String path, ElementType elementType, Bitmap bitmap) {
    }

    private static final String image_uri = "image_uri";
    private static final String audio_uri = "audio_uri";
    private static final String video_uri = "video_uri";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable(image_uri, imageUri);
        outState.putParcelable(audio_uri, audioUri);
        outState.putParcelable(video_uri, videoUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageUri = savedInstanceState.getParcelable(image_uri);
        audioUri = savedInstanceState.getParcelable(audio_uri);
        videoUri = savedInstanceState.getParcelable(video_uri);

        if (imageUri != null)
            handleTempUri(imageUri);

        if (audioUri != null)
            handleAudioTempUri(audioUri);

        if (videoUri != null)
            handleVideoTempUri(videoUri);

    }

    private void handleTempUri(Uri tempUri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);

            ExifInterface exif = new ExifInterface(tempUri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap resizedBitmap = BitmapUtils.scaleBitmap(bitmap);

            Bitmap bitmap1 = BitmapUtils.rotateBitmap(resizedBitmap, orientation);

            if (bitmap1 != null)
                bitmap = bitmap1;

            String path = FileUtils.getRealPath(this, tempUri);
            ElementType type = ElementType.img;

            pickerResult(path, type, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAudioTempUri(Uri audioTempUri) {

        try {

            final String path = FileUtils.getRealPath(this, audioTempUri);

            final ElementType type = ElementType.audio;

            pickerResult(path, type, null);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleVideoTempUri(Uri videoTempUri) {
        try {

            final String path = FileUtils.getRealPath(this, videoTempUri);

            final ElementType type = ElementType.vid;

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Images.Thumbnails.MINI_KIND);

            pickerResult(path, type, thumb);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
