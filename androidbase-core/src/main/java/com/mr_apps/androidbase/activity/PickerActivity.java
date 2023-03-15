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
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import com.mr_apps.androidbase.utils.BitmapUtils;
import com.mr_apps.androidbase.utils.FileUtils;
import com.mr_apps.androidbase.utils.FileUtils.ElementType;
import com.mr_apps.androidbasecore.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Base activity that offers method to manage taking pictures and choosing images from gallery
 * Warning: To avoid activity recreation remember to add "android:configChanges="orientation|keyboardHidden|screenSize" in your manifest.
 *
 * @author Denis Brandi
 * @author Mattia Ruggiero
 */
public abstract class PickerActivity extends LocationActivity {

    private static final int COMPRESSION_QUALITY = 80;
    private static final int COMPRESSION_WIDTH = 1280;
    private static final int COMPRESSION_HEIGHT = 1280;

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

    private int qualityImage = COMPRESSION_QUALITY;
    private int imageWidth = COMPRESSION_WIDTH;
    private int imageHeight = COMPRESSION_HEIGHT;

    /**
     * Sets the quality of the image taken or chosen from gallery
     *
     * @param qualityImage the quality of the image
     */
    public void setQualityImage(int qualityImage) {
        this.qualityImage = qualityImage;
    }

    /**
     * Sets the width of the image taken or chosen from gallery
     *
     * @param imageWidth the width of the image
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * Sets the height of the image taken or chosen from gallery
     *
     * @param imageHeight the height of the image
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    private boolean saveInInternalStorage = false;

    /**
     * Shows an alert dialog that allows the user to choose an action. The possible actions are determined by the parameters of the method
     *
     * @param title                the title of the dialog
     * @param pickImageFromGallery true if the dialog should contain the option for picking an image from gallery, false otherwise
     * @param takePhoto            true if the dialog should contain the option for taking a photo using camera, false otherwise
     */
    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto) {
        showAlertChoice(title, pickImageFromGallery, takePhoto, false, false);
    }

    /**
     * Shows an alert dialog that allows the user to choose an action. The possible actions are determined by the parameters of the method
     *
     * @param title                the title of the dialog
     * @param pickImageFromGallery true if the dialog should contain the option for picking an image from gallery, false otherwise
     * @param takePhoto            true if the dialog should contain the option for taking a photo using camera, false otherwise
     * @param pickVideoFromGallery true if the dialog should contain the option for picking a video from gallery, false otherwise
     * @param recordVideo          true if the dialog should contain the option for record a video, false otherwise
     */
    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto, boolean pickVideoFromGallery, boolean recordVideo) {
        showAlertChoice(title, pickImageFromGallery, takePhoto, pickVideoFromGallery, recordVideo, false, false);
    }

    /**
     * Shows an alert dialog that allows the user to choose an action. The possible actions are determined by the parameters of the method
     *
     * @param title                the title of the dialog
     * @param pickImageFromGallery true if the dialog should contain the option for picking an image from gallery, false otherwise
     * @param takePhoto            true if the dialog should contain the option for taking a photo using camera, false otherwise
     * @param pickVideoFromGallery true if the dialog should contain the option for picking a video from gallery, false otherwise
     * @param recordVideo          true if the dialog should contain the option for record a video, false otherwise
     * @param pickAudioFromGallery true if the dialog should contain the option for picking an audio from gallery, false otherwise
     * @param recordAudio          true if the dialog should contain the option for record an audio file, false otherwise
     */
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

    /**
     * Opens the screen where the user can choose a photo from his gallery
     */
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

    /**
     * Opens the camera activity where the user can take a photo to return to this activity
     */
    public void takePhoto() {
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.img, saveInInternalStorage);
        imageUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, actionTakeImage);
    }

    /**
     * Opens the screen where the user can choose a video from his gallery
     */
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

    /**
     * Opens the camera activity where the user can record a video to return to this activity
     */
    public void recordVideo() {
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.vid, false);
        videoUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, getDurationVideo());
        startActivityForResult(intent, actionRecordVideo);
    }

    /**
     * Opens the screen where the user can choose an audio file from his gallery
     */
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

    /**
     * Opens the camera activity where the user can record an audio file to return to this activity
     */
    public void recordAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.audio, false);
        audioUri = Uri.fromFile(file);
        Uri intentUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, intentUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, getDurationAudio());
        startActivityForResult(intent, actionRecordAudio);
    }

    /**
     * Gets the duration of the video. The default value is 30, but is strongly recommended to override this method in the subclass to customize the value
     *
     * @return the default video duration, 30
     */
    public int getDurationVideo() {
        return 30;
    }

    /**
     * Gets the duration of the audio file. The default value is 120, but is strongly recommended to override this method in the subclass to customize the value
     *
     * @return the default audio file duration, 120
     */
    public int getDurationAudio() {
        return 120;
    }

    /**
     * Gets the folder where the images or the video have to be saved.
     * The default folder is the root folder, so is recommended to override this method in the subclass to customize the default folder
     *
     * @return an empty string, so the default folder is the root folder
     */
    public String getFolder() {
        return "";
    }

    /**
     * Gets the title of the chooser dialog
     *
     * @return the title of the chooser dialog
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * Setter method for the "saveInInternalStorage" flag
     *
     * @param saveInInternalStorage true if the file should be saved in the internal storage, false otherwise
     */
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

                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    onImageSelected(bitmap);

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

    private void onImageSelected(Bitmap bitmap) {
        File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.img, saveInInternalStorage);

        try {

            Bitmap resizedBitmap = BitmapUtils.scaleBitmap(bitmap, imageWidth, imageHeight);

            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, qualityImage, os);
            os.close();

            pickerResult(file.getPath(), ElementType.img, resizedBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that should be override by the subclasses to manage the image taken from camera or picked from gallery
     *
     * @param path        the path where the image is saved
     * @param elementType the type of the bitmap file
     * @param bitmap      the bitmap image
     */
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

    /**
     * Manages the temporary Uri of the bitmap image, setting it in the correct orientation and resizing it before calling the "pickerResult" method
     *
     * @param tempUri the temporary Uri of the image
     */
    private void handleTempUri(Uri tempUri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);

            ExifInterface exif = new ExifInterface(tempUri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap resizedBitmap = BitmapUtils.scaleBitmap(bitmap, imageWidth, imageHeight);

            Bitmap bitmap1 = BitmapUtils.rotateBitmap(resizedBitmap, orientation);

            if (bitmap1 != null)
                bitmap = bitmap1;

            File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.img, saveInInternalStorage);

            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, qualityImage, os);
            os.close();

            pickerResult(file.getPath(), ElementType.img, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manages the temporary Uri of the audio file, before calling the "pickerResult" method
     *
     * @param audioTempUri the temporary Uri of the audio file
     */
    private void handleAudioTempUri(Uri audioTempUri) {

        try {

            final String path = FileUtils.getRealPath(this, audioTempUri);

            final ElementType type = ElementType.audio;

            pickerResult(path, type, null);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Manages the temporary Uri of the video, before calling the "pickerResult" method
     *
     * @param videoTempUri the temporary Uri of the video
     */
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
