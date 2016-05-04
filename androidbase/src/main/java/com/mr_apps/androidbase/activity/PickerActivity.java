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
import com.mr_apps.androidbase.utils.FileUtils;
import com.mr_apps.androidbase.utils.FileUtils.ElementType;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by denis on 29/02/16
 */
public abstract class PickerActivity extends LocationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageUri=null;
        videoUri=null;
    }

    private boolean pickImageFromGallery, takePhoto, pickVideoFromGallery, recordVideo;
    private String mTitle;

    private Uri imageUri, videoUri;

    private static final int actionPickImage = 100;
    private static final int actionTakeImage = 200;
    private static final int actionPickVideo = 300;
    private static final int actionRecordVideo = 400;

    private int qualityImage=80;

    public void setQualityImage(int qualityImage) {
        this.qualityImage = qualityImage;
    }

    private boolean saveInInternalStorage=false;

    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto, boolean pickVideoFromGallery, boolean recordVideo) {

        this.pickImageFromGallery = pickImageFromGallery;
        this.takePhoto = takePhoto;
        this.pickVideoFromGallery = pickVideoFromGallery;
        this.recordVideo = recordVideo;
        this.mTitle = title;

        imageUri = null;
        videoUri = null;

        if (!checkOrRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_storage))
            return;

        final String scegli_immagine = getString(R.string.Scegli_immagine_da_galleria);
        final String scatta_foto = getString(R.string.Scatta_foto);
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

                        } else if (choice.equals(scatta_foto)) {

                            File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.img, saveInInternalStorage);
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            imageUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(intent, actionTakeImage);

                        } else if (choice.equals(scegli_video)) {

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

                        } else if (choice.equals(gira_video)) {

                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            File file = FileUtils.newFileToUpload(PickerActivity.this, getFolder(), ElementType.vid, false);
                            videoUri = Uri.fromFile(file);
                            //Uri outputFileUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, getDurationVideo());
                            startActivityForResult(intent, actionRecordVideo);

                        }

                    }
                }).show();

    }

    public int getDurationVideo() {
        return 30;
    }

    public String getFolder() {
        return "";
    }

    public String getmTitle() {
        return mTitle;
    }

    public boolean isPickImageFromGallery() {
        return pickImageFromGallery;
    }

    public boolean isTakePhoto() {
        return takePhoto;
    }

    public boolean isPickVideoFromGallery() {
        return pickVideoFromGallery;
    }

    public boolean isRecordVideo() {
        return recordVideo;
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
                    type = ElementType.img;

                    pickerResult(path, type, null);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void pickerResult(String path, ElementType elementType, Bitmap bitmap) {
    }

    private static final String file_uri1 = "image_uri";
    private static final String file_uri2 = "video_uri";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable(file_uri1, imageUri);
        outState.putParcelable(file_uri2, videoUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageUri = savedInstanceState.getParcelable(file_uri1);
        videoUri = savedInstanceState.getParcelable(file_uri2);

        if(imageUri!=null)
            handleTempUri(imageUri);

        if(videoUri!=null)
            handleVideoTempUri(videoUri);

    }

    /*private void handleTempUri(Uri tempUri, boolean isImage) {

        if (tempUri == null)
            return;

        File file;
        String path = null;
        ElementType type = null;
        Bitmap bitmap = null;

        if (isImage) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);

                ExifInterface exif = new ExifInterface(tempUri.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                bitmap = FileUtils.rotateBitmap(bitmap, orientation);

                file = FileUtils.generateFileFromBitmap(this, getFolder(), bitmap, saveInInternalStorage, qualityImage);

                File file1=new File(tempUri.getPath());

                if(file.getParent().equals(file1.getParent()))
                    file1.delete();

                path = file.getPath();
                type = ElementType.img;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            path = FileUtils.getRealPath(this, tempUri);
            type = ElementType.vid;

        }

        pickerResult(path, type, bitmap);

    }*/

    private void handleTempUri(Uri tempUri)
    {
        //photo = null;//(Bitmap) data.getExtras().get("data");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);

            ExifInterface exif = new ExifInterface(tempUri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap resizedBitmap=FileUtils.scaleBitmap(bitmap);

            Bitmap bitmap1=FileUtils.rotateBitmap(resizedBitmap, orientation);

            if(bitmap1!=null)
                bitmap = bitmap1;

            String path=FileUtils.getRealPath(this, tempUri);
            ElementType type=ElementType.img;

            pickerResult(path, type, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVideoTempUri(Uri videoTempUri) {
        try {
            Uri filmedVideo = videoTempUri;

            final String path = FileUtils.getRealPath(this, filmedVideo);

            final ElementType type = ElementType.vid;

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Images.Thumbnails.MINI_KIND);

            pickerResult(path, type, thumb);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
