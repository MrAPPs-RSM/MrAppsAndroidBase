package com.mr_apps.androidbase.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mr_apps.androidbasecore.R;
import com.mr_apps.androidbase.preferences.GlobalPreferences;
import com.mr_apps.androidbase.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.concurrent.FutureCallback;


/**
 * Base Activity that manage all the location's stuff
 *
 * @author Denis Brandi
 */
@SuppressWarnings("ALL")
public abstract class LocationActivity extends PermissionManagerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public String location;
    public LatLng latLng;
    public double altitude;

    private static final String TAG = "LocationActivity";

    /**
     * Creates google API client with customizable params
     */
    protected synchronized void createGoogleApiClient() {
        //Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(getInterval());
        mLocationRequest.setFastestInterval(getFastestInterval());
        mLocationRequest.setSmallestDisplacement(getSmallestDisplacement());
        mLocationRequest.setPriority(getPriority());

        mGoogleApiClient.connect();
    }

    /**
     * Method that should be overrided by the subclasses to customize the interval of the Google Api Client
     *
     * @return the interval of the Google Api Client
     */
    public long getInterval() {
        return 60000;
    }

    /**
     * Method that should be overrided by the subclasses to customize the fastest interval of the Google Api Client
     *
     * @return the fastest interval of the Google Api Client
     */
    public long getFastestInterval() {
        return 30000;
    }

    /**
     * Method that should be overrided by the subclasses to customize the smallest displacement of the Google Api Client
     *
     * @return the smallest displacement of the Google Api Client
     */
    public float getSmallestDisplacement() {
        return 100;
    }

    /**
     * Gets the priority of the location request
     *
     * @return the priority of the location request
     */
    public int getPriority() {
        return LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
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

    /**
     * Sets the global preferences connected to the location stuff
     */
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

        if (!isGpsActivated() && isLocationEnabled()) {
            buildAlertMessageNoGps();
        }
    }

    /**
     * Method that control if the GPS is activated or not
     *
     * @return true if the GPS is active, false otherwise
     */
    public boolean isGpsActivated() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Creates a Google Api Client
     */
    public void refreshPosition() {
        createGoogleApiClient();
    }

    /**
     * Shows an alert message is the GPS is not activated, that asks the user if he wants to activate the GPS
     */
    public void buildAlertMessageNoGps() {
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
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient == null)
            return;

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLocationEnabled())
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
                mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isLocationEnabled())
            stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isLocationEnabled())
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                mGoogleApiClient.disconnect();
    }

    /**
     * Starts the location updates for this activity
     *
     * @throws SecurityException if some security error occur
     */
    protected void startLocationUpdates() throws SecurityException {
        if (isLocationEnabled()) {
            if (mGoogleApiClient == null)
                return;

            if (!mGoogleApiClient.isConnected())
                return;

            if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                return;

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stops the location updates for this activity
     */
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
        altitude = location.getAltitude();

        Logger.d(TAG, latLng.toString());
        Logger.d(TAG, "altitude: " + String.valueOf(altitude));

        onPositionFound();

    }

    /**
     * Sets the title of the toolbar to the location name
     */
    public void setLocationNameToTitle() {

        getStringFromLocation(latLng.latitude, latLng.longitude, new FutureCallback<List<Address>>() {
            @Override
            public void completed(List<Address> listAddresses) {


                if (null != listAddresses && listAddresses.size() > 0) {
                    location = listAddresses.get(0).getLocality();
                    setTitle(location);
                    //getSupportActionBar().setTitle();
                }

            }

            @Override
            public void failed(Exception ex) {

            }

            @Override
            public void cancelled() {

            }
        });

    }

    /**
     * Method that should be override to set the title of the toolbar
     *
     * @param title the title of the toolbar
     */
    public void setTitle(String title) {
    }

    /**
     * Method that should be override by the subclasses to sets if the location is enabled or not for this activity
     *
     * @return false
     */
    public boolean isLocationEnabled() {
        return false;
    }

    /**
     * Takes some coordinates and create a string containing the full name of the location
     *
     * @param lat      the latitude
     * @param lng      the longitude
     * @param complete the callback for the caller of the method
     */
    public void getStringFromLocation(double lat, double lng, final FutureCallback<List<Address>> complete) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(this, String
                .format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                        + Locale.getDefault().getCountry(), lat, lng), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Gson gson = new GsonBuilder().create();

                if (responseString == null) {
                    complete.failed(null);
                    return;
                }

                JsonElement element = gson.fromJson(responseString, JsonElement.class);

                if (!element.isJsonObject()) {
                    complete.failed(null);
                    return;
                }

                JsonObject result = element.getAsJsonObject();

                ArrayList<Address> retList = new ArrayList<>();

                JsonArray results = result.getAsJsonArray("results");

                if (results != null) {
                    for (JsonElement el : results) {
                        JsonObject object = el.getAsJsonObject();

                        String indiStr = object.get("formatted_address").getAsString();
                        Address addr = new Address(Locale.getDefault());
                        addr.setAddressLine(0, indiStr);

                        JsonArray addressComponents = object.getAsJsonArray("address_components");

                        //civico
                        if (addressComponents.size() > 0)
                            addr.setPremises(addressComponents.get(0).getAsJsonObject().get("long_name").getAsString());

                        //via
                        if (addressComponents.size() > 1)
                            addr.setThoroughfare(addressComponents.get(1).getAsJsonObject().get("long_name").getAsString());

                        //zona
                        if (addressComponents.size() > 2)
                            addr.setLocality(addressComponents.get(2).getAsJsonObject().get("long_name").getAsString());

                        //citta
                        if (addressComponents.size() > 3)
                            addr.setCountryName(addressComponents.get(3).getAsJsonObject().get("long_name").getAsString());
                        //cap
                        if (addressComponents.size() > 4)
                            addr.setPostalCode(addressComponents.get(4).getAsJsonObject().get("long_name").getAsString());

                        retList.add(addr);

                    }
                }

                complete.completed(retList);

            }
        });
    }

}
