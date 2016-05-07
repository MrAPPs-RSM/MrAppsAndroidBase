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
 * Class that manages all the base runtime permissions for API > 23 devices
 *
 * @author Mattia Ruggiero
 */
public abstract class PermissionManagerActivity extends AppCompatActivity {

    private static final int REQUEST_APP_SETTINGS = 1;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 1;
    private static final int READ_STORAGE_PERMISSION_REQUEST = 2;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 3;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 4;
    private static final int CAMERA = 5;

    /**
     * Checks if a permission has already been accepted by the user, and request it if not
     *
     * @param permission the permission that the user must accept
     * @param titleId the string's resource id of the title of the dialog to show, if the permission was denied in the past
     * @param messageId the string's resource id of the message of the dialog to show, if the permission was denied in the past
     * @return true if the permission was already accepted, false otherwise
     */
    public boolean checkOrRequestPermission(String permission, int titleId, int messageId) {
        return checkOrRequestPermission(permission, getString(titleId), getString(messageId));
    }

    /**
     * Checks if a permission has already been accepted by the user, and request it if not
     *
     * @param permission the permission that the user must accept
     * @param title the title of the dialog to show, if the permission was denied in the past
     * @param message the message of the dialog to show, if the permission was denied in the past
     * @return true if the permission was already accepted, false otherwise
     */
    public boolean checkOrRequestPermission(String permission, String title, String message) {

        if (checkPermission(permission))
            return true;
        else {
            int permissionRequest = getPermissionRequestByPermissionName(permission);
            requestPermission(permission, permissionRequest, title, message);
            return false;
        }
    }

    /**
     * Gets the permission request's id from the permission's string
     *
     * @param permission the string of the permission
     * @return the id of the permission's request
     */
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

    /**
     * Checks if the system already has the permission
     *
     * @param permission the permission yo check
     * @return true if the permission already has been accepted, false otherwise
     */
    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /*
    se non ho il permesso lo richiedo

    -forceAlert=true: se non ho il permesso e l'utente precedentemente me lo ha rifiutato glielo chiedo lo stesso
    -forceAlert=false: se non ho il permesso e l'utente precedentemente me lo ha rifiutato non glielo richiedo
     */

    /**
     * Requests the given permission to the user
     *
     * @param permission the permission to request
     * @param MY_PERMISSIONS_REQUEST_CODE the code of the permission
     * @param titleId the string's resource id of the title of the dialog to show, if the permission was denied in the past
     * @param messageId the string's resource id of the message of the dialog to show, if the permission was denied in the past
     */
    private void requestPermission(String permission, int MY_PERMISSIONS_REQUEST_CODE, int titleId, int messageId) {
        requestPermission(permission, MY_PERMISSIONS_REQUEST_CODE, getString(titleId), getString(messageId));
    }

    /**
     * Requests the given permission to the user
     *
     * @param permission the permission to request
     * @param MY_PERMISSIONS_REQUEST_CODE the code of the permission
     * @param title the title of the dialog to show, if the permission was denied in the past
     * @param message the message of the dialog to show, if the permission was denied in the past
     */
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

    /**
     * Requests the given permission to the user using the standard dialog
     *
     * @param permission the permission to request
     * @param MY_PERMISSIONS_REQUEST_CODE the code of the permission
     */
    private void requestPermissionWithAlert(String permission, int MY_PERMISSIONS_REQUEST_CODE) {
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                MY_PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Shows a dialog to tell the user that he MUST accept permission in order to use the application
     *
     * @param permission the permission to accept
     * @param titleId the string's resource id of the title of the dialog
     * @param messageId the string's resource id of the message of the dialog
     */
    private void userMustAcceptPermission(String permission, int titleId, int messageId) {
        userMustAcceptPermission(permission, getString(titleId), getString(messageId));
    }

    /**
     * Shows a dialog to tell the user that he MUST accept permission in order to use the application
     *
     * @param permission the permission to accept
     * @param title the title of the dialog
     * @param message the message of the dialog
     */
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

    /**
     * Calls the right methods that has to be overriden by the subclasses
     *
     * @param requestCode the permission id
     * @param granted true if the permission was granted, false otherwise
     */
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

    /**
     * Go to the settings activity of the phone, to help the user accept the permission
     */
    private void goToSettings() {

        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    /**
     * Method that should be overriden by the subclasses to manage the acceptance of the write storage permission
     *
     * @param granted true if the permission was granted, false otherwise
     */
    public void writeStoragePermissionResult(boolean granted) {}

    /**
     * Method that should be overriden by the subclasses to manage the acceptance of the read storage permission
     *
     * @param granted true if the permission was granted, false otherwise
     */
    public void readStoragePermissionResult(boolean granted) {}

    /**
     * Method that should be overriden by the subclasses to manage the acceptance of the access coarse permission
     *
     * @param granted true if the permission was granted, false otherwise
     */
    public void accessCoarsePermissionResult(boolean granted) {}

    /**
     * Method that should be overriden by the subclasses to manage the acceptance of the access fine permission
     *
     * @param granted true if the permission was granted, false otherwise
     */
    public void accessFinePermissionResult(boolean granted) {}

    /**
     * Method that should be overriden by the subclasses to manage the acceptance of the camera permission
     *
     * @param granted true if the permission was granted, false otherwise
     */
    public void cameraPermissionResult(boolean granted){}

}
