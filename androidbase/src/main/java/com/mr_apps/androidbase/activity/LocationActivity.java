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
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.preferences.GlobalPreferences;
import com.mr_apps.androidbase.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by denis on 15/01/16
 */
public abstract class LocationActivity extends PermissionManagerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public String location;
    public GoogleMap googleMap;
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

        if (!isGpsActivated() && isLocationEnabled()) {
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
        if(isLocationEnabled())
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
                mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isLocationEnabled())
            stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isLocationEnabled())
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                mGoogleApiClient.disconnect();
    }

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

    public void setLocationNameToTitle() {

        getStringFromLocation(latLng.latitude, latLng.longitude, new FutureCallback<List<Address>>() {
            @Override
            public void onCompleted(Exception e, List<Address> listAddresses) {


                if (null != listAddresses && listAddresses.size() > 0) {
                    location = listAddresses.get(0).getLocality();
                    setTitle(location);
                    //getSupportActionBar().setTitle();
                }

            }
        });

    }

    public void setTitle(String title){}

    public boolean isLocationEnabled(){
        return false;
    }

    public void getStringFromLocation(double lat, double lng, final FutureCallback<List<Address>> complete) {

        Ion.with(this)
                .load(String
                        .format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                                + Locale.getDefault().getCountry(), lat, lng))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result == null) {
                            complete.onCompleted(e, null);
                            return;
                        }

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

                        complete.onCompleted(e, retList);

                    }
                });

        /*String address = String
                .format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                        + Locale.getDefault().getCountry(), lat, lng);
        HttpGet httpGet = new HttpGet(address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        List<Address> retList = null;

        response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
        int b;
        while ((b = stream.read()) != -1) {
            stringBuilder.append((char) b);
        }

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        retList = new ArrayList<Address>();

        if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String indiStr = result.getString("formatted_address");
                Address addr = new Address(Locale.getDefault());
                addr.setAddressLine(0, indiStr);
                retList.add(addr);
            }
        }

        return retList;*/
    }

}
