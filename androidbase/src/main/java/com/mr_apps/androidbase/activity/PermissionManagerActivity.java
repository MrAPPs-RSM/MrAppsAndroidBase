package com.mr_apps.androidbase.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by denis on 15/01/16
 */
public abstract class PermissionManagerActivity extends AppCompatActivity {

    private static final int REQUEST_APP_SETTINGS = 1;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 1;
    private static final int READ_STORAGE_PERMISSION_REQUEST = 2;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 3;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 4;
    private static final int CAMERA = 5;

    public boolean checkOrRequestPermission(String permission, int titleId, int messageId) {
        return checkOrRequestPermission(permission, getString(titleId), getString(messageId));
    }

    public boolean checkOrRequestPermission(String permission, String title, String message) {

        if (checkPermission(permission))
            return true;
        else {
            int permissionRequest = getPermissionRequestByPermissionName(permission);
            requestPermission(permission, permissionRequest, title, message);
            return false;
        }
    }

    private int getPermissionRequestByPermissionName(String permission) {

        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return WRITE_STORAGE_PERMISSION_REQUEST;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return READ_STORAGE_PERMISSION_REQUEST;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return ACCESS_COARSE_LOCATION_PERMISSION_REQUEST;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return ACCESS_FINE_LOCATION_PERMISSION_REQUEST;
            case Manifest.permission.CAMERA:
                return CAMERA;

            default:
                return 0;
        }

    }

    //controllo se ho già il permesso

    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /*
    se non ho il permesso lo richiedo

    -forceAlert=true: se non ho il permesso e l'utente precedentemente me lo ha rifiutato glielo chiedo lo stesso
    -forceAlert=false: se non ho il permesso e l'utente precedentemente me lo ha rifiutato non glielo richiedo
     */

    private void requestPermission(String permission, int MY_PERMISSIONS_REQUEST_CODE, int titleId, int messageId) {
        requestPermission(permission, MY_PERMISSIONS_REQUEST_CODE, getString(titleId), getString(messageId));
    }

    private void requestPermission(String permission, int MY_PERMISSIONS_REQUEST_CODE, String title, String message) {
        // Here, thisActivity is the current activity
        if (!checkPermission(permission)) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                userMustAcceptPermission(permission, title, message);


            } else {

                // No explanation needed, we can request the permission.

                requestPermissionWithAlert(permission, MY_PERMISSIONS_REQUEST_CODE);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    private void requestPermissionWithAlert(String permission, int MY_PERMISSIONS_REQUEST_CODE) {
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                MY_PERMISSIONS_REQUEST_CODE);
    }

    private void userMustAcceptPermission(String permission, int titleId, int messageId) {
        userMustAcceptPermission(permission, getString(titleId), getString(messageId));
    }

    private void userMustAcceptPermission(final String permission, String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToPermissionCallback(getPermissionRequestByPermissionName(permission), false);
                        //if (callback != null)
                        //callback.permissionDenied();
                    }
                })
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSettings();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean granted = grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        goToPermissionCallback(requestCode, granted);

    }

    private void goToPermissionCallback(int requestCode, boolean granted) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST: {
                writeStoragePermissionResult(granted);
                break;
            }

            case READ_STORAGE_PERMISSION_REQUEST: {
                readStoragePermissionResult(granted);
                break;
            }

            case ACCESS_COARSE_LOCATION_PERMISSION_REQUEST: {
                accessCoarsePermissionResult(granted);
                break;
            }

            case ACCESS_FINE_LOCATION_PERMISSION_REQUEST: {
                accessFinePermissionResult(granted);
                break;
            }

            case CAMERA: {
                cameraPermissionResult(granted);
                break;
            }
        }
    }

    private void goToSettings() {

        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    //metodi granted-denied

    public void writeStoragePermissionResult(boolean granted) {}

    public void readStoragePermissionResult(boolean granted) {}

    public void accessCoarsePermissionResult(boolean granted) {}

    public void accessFinePermissionResult(boolean granted) {}

    public void cameraPermissionResult(boolean granted){}

}
