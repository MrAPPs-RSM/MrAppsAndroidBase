package com.mr_apps.androidbase.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.utils.LocalUploader;
import com.mr_apps.androidbase.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by denis on 29/02/16.
 */
public abstract class PickerActivity extends LocationActivity {

    boolean pickImageFromGallery, takePhoto, pickVideoFromGallery, recordVideo;
    String title;

    private Uri imageUri, videoUri;

    private static final int actionPickImage=100;
    private static final int actionTakeImage=200;
    private static final int actionPickVideo=300;
    private static final int actionRecordVideo=400;

    public void showAlertChoice(String title, boolean pickImageFromGallery, boolean takePhoto, boolean pickVideoFromGallery, boolean recordVideo)
    {

        this.pickImageFromGallery=pickImageFromGallery;
        this.takePhoto=takePhoto;
        this.pickVideoFromGallery=pickVideoFromGallery;
        this.recordVideo=recordVideo;
        this.title=title;

        imageUri=null;
        videoUri=null;

        if(!checkOrRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_storage))
            return;

        final String scegli_immagine=getString(R.string.Scegli_immagine_da_galleria);
        final String scatta_foto=getString(R.string.Scatta_foto);
        final String scegli_video=getString(R.string.Scegli_video_da_galleria);
        final String gira_video=getString(R.string.Gira_video);

        ArrayList<String> itemsArrayList=new ArrayList<>();

        if(pickImageFromGallery)
            itemsArrayList.add(scegli_immagine);

        if(takePhoto)
            itemsArrayList.add(scatta_foto);

        if(pickVideoFromGallery)
            itemsArrayList.add(scegli_video);

        if(recordVideo)
            itemsArrayList.add(gira_video);

        final String [] items=new String[itemsArrayList.size()];

        for(int i=0; i<itemsArrayList.size(); i++)
            items[i]=itemsArrayList.get(i);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String choice=items[which];

                        if(choice.equals(scegli_immagine))
                        {
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

                        } else if(choice.equals(scatta_foto)) {

                            File file = Utils.newFileToUpload(PickerActivity.this, getFolder(), Utils.ElementType.img);
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            imageUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(intent, actionTakeImage);

                        } else if(choice.equals(scegli_video)) {

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

                        } else if(choice.equals(gira_video)) {

                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            File file = Utils.newFileToUpload(PickerActivity.this, getFolder(), Utils.ElementType.vid);
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

    public String getFolder()
    {
        return "";
    }

    @Override
    public void writeStoragePermissionResult(boolean granted) {
        super.writeStoragePermissionResult(granted);

        if(granted)
            showAlertChoice(title, pickImageFromGallery, takePhoto, pickVideoFromGallery, recordVideo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= RESULT_OK)
            return;

        File file;
        String path;
        Utils.ElementType type;

        switch (requestCode) {

            case actionTakeImage:
                handleTempUri();
                break;
            case actionRecordVideo:
                handleTempUri();
                break;

            case actionPickImage:

                Uri uri=data.getData();

                try {

                    LocalUploader.MediaSelected mediaSelected = LocalUploader.getPath(this, uri);
                    path = mediaSelected.path;
                    type = Utils.ElementType.img;

                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    pickerResult(path, type, bitmap);


                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;

            case actionPickVideo:

                Uri uriVideo=data.getData();

                try {

                    LocalUploader.MediaSelected mediaSelected = LocalUploader.getPath(this, uriVideo);
                    path = mediaSelected.path;
                    type = Utils.ElementType.img;

                    pickerResult(path, type, null);


                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void pickerResult(String path, Utils.ElementType elementType, Bitmap bitmap){}

    private static final String file_uri1 = "image_uri";
    private static final String file_uri2 = "video_uri";
    private static final String date_uri1 = "date_uri1";
    private static final String date_uri2 = "date_uri2";

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageUri = savedInstanceState.getParcelable(file_uri1);
        videoUri = savedInstanceState.getParcelable(file_uri2);

        handleTempUri();
    }

    private void handleTempUri()
    {
        Uri tempUri=imageUri==null?videoUri:imageUri;

        if(tempUri==null)
            return;

        File file;
        String path=null;
        Utils.ElementType type=null;
        Bitmap bitmap = null;

        if(imageUri!=null) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);


                ExifInterface exif = new ExifInterface(tempUri.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                bitmap = LocalUploader.rotateBitmap(bitmap, orientation);

                file = LocalUploader.generateFileFromBitmap(this, getFolder(), bitmap);
                path = file.getPath();
                type = Utils.ElementType.img;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            path= LocalUploader.getRealPath(this, tempUri);
            type= Utils.ElementType.vid;

        }

        pickerResult(path, type, bitmap);

    }

}
