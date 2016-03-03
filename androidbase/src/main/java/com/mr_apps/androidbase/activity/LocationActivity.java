package com.mr_apps.androidbase.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.preferences.GlobalPreferences;
import com.mr_apps.androidbase.utils.Logger;


/**
 * Created by denis on 15/01/16
 */
public abstract class LocationActivity extends PermissionManagerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public LatLng latLng;
    public double altitude;

    private static final String TAG = "LocationActivity";

    protected synchronized void createGoogleApiClient() {
        //Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();

        if (checkOrRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, R.string.Titolo_permesso_obbligatorio, R.string.Messaggio_permesso_posizione)) {
            accessFinePermissionResult(true);
        }
    }

    @Override
    public void accessFinePermissionResult(boolean granted) throws SecurityException {
        super.accessFinePermissionResult(granted);

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            if (!isGpsActivated()) {
                onPositionFound();
                return;
            }

            //currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        startLocationUpdates();

    }

    public void onPositionFound() {
        GlobalPreferences.setLatitude(this, String.valueOf(latLng.latitude));
        GlobalPreferences.setLongitude(this, String.valueOf(latLng.longitude));
        GlobalPreferences.setAltitude(this, String.valueOf(altitude));
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGpsActivated()) {
            buildAlertMessageNoGps();
        }
    }

    public boolean isGpsActivated() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void refreshPosition() {
        createGoogleApiClient();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.Avviso_gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Si), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.Annulla), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    protected void startLocationUpdates() throws SecurityException {

        if (mGoogleApiClient == null)
            return;

        if (!mGoogleApiClient.isConnected())
            return;

        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {

        if (mGoogleApiClient == null)
            return;

        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        altitude=location.getAltitude();

        Logger.d(TAG, latLng.toString());
        Logger.d(TAG, "altitude: "+String.valueOf(altitude));

        onPositionFound();

    }

}
