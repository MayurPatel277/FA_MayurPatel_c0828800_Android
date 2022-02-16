package com.example.fa_mayurpatel_c0828800_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    Marker mCurrLocationMarker, updateMarker;
    String mapType;
    DataBaseHelper dataBaseHelper;
    DataBaseModel model;
    private TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        distance = findViewById(R.id.map_distance);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapType = bundle.getString("TYPE", "");
            model = bundle.getParcelable("MODEL");

            if (mapType.equalsIgnoreCase("")) {

                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Alert")
                        .setMessage("Hold the Icon and drag it to update new location")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();

            } else {

                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Alert")
                        .setMessage("Long click on map to insert data and show marker")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();


            }
        }

        dataBaseHelper = new DataBaseHelper(MapsActivity.this);

        getLocation();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if (mapType.equalsIgnoreCase("DEFALUT VIEW")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (mapType.equalsIgnoreCase("SATELIGHT VIEW")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (mapType.equalsIgnoreCase("HYBRID VIEW")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else if (mapType.equalsIgnoreCase("TERRAIN VIEW")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }

        if (mapType.equalsIgnoreCase("")) {

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Place current location marker
            LatLng latLng = new LatLng(Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(AddressModel.getAddress(Double.parseDouble(model.getLng()), Double.parseDouble(model.getLng()), MapsActivity.this));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            markerOptions.draggable(true);
            updateMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }

    private void getLocation() {
        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
//        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            Toast.makeText(MapsActivity.this, "Got Location", Toast.LENGTH_SHORT).show();

                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            if (mCurrLocationMarker != null) {
                                mCurrLocationMarker.remove();
                            }
                            //Place current location marker
                            LatLng latLng = new LatLng(lati, longi);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(AddressModel.getAddress(lati, longi, MapsActivity.this));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                            mCurrLocationMarker = mMap.addMarker(markerOptions);

                            //move map camera
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                            if (model != null) {
                                float[] results = new float[1];
                                Location.distanceBetween(lati, longi, Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()), results);
                                distance.setText(String.format("Distance: %f meters", results[0]));
                            } else {
                                distance.setVisibility(View.GONE);
                            }
                            //fetchaddressfromlocation(location);

                        } else {

                        }
                    }
                }, Looper.getMainLooper());

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(AddressModel.getAddress(lat, lng, MapsActivity.this))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        dataBaseHelper.insert(AddressModel.getAddress(lat, lng, MapsActivity.this), String.valueOf(lat), String.valueOf(lng), 0, 0);
        Toast.makeText(MapsActivity.this, "SELECTED LOCATION IS ADDED IN YOU FAVOURITE PLACE LIST", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        updateMarker.remove();


        MarkerOptions markerOptions = new MarkerOptions().position(marker.getPosition()).title("New Location").draggable(true);
        markerOptions.position(marker.getPosition());
        markerOptions.title("New Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptions.draggable(true);
        updateMarker = mMap.addMarker(markerOptions);

        dataBaseHelper.updatePlace(model, AddressModel.getAddress(marker.getPosition().latitude, marker.getPosition().longitude, MapsActivity.this), String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude));
        Toast.makeText(MapsActivity.this, "CHANGES IN LOCATION IS UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}