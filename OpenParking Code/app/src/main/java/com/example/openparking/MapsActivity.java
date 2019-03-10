package com.example.openparking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private static final String TAG = "Maps Activity";


    // Test Values for Random Markers in Long Beach
    double scale_value  = 1000000.0;
    int parallel = 33;
    int meridian = -118;

    //Test Area from (Hilltop Park) to (7th and Studebaker)
    // Latitude Range
    private static final int maxLat =800471;
    private static final int minLat =774571;
    // Longitude Range
    private static final int maxLon =167585;
    private static final int minLon =103137;

    private static final int NUM_MARKERS = 8;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set a preference for minimum and maximum zoom.
        //mMap.setMinZoomPreference(6.0f);
        //mMap.setMaxZoomPreference(14.0f);
    }

    private LatLng randomLongBeachLocation()
    {
        int randLat = new Random().nextInt(maxLat-minLat)+minLat;
        int randLon = new Random().nextInt(maxLon-minLon)+minLon;
        double randLatD = (double)(randLat/scale_value) + parallel;
        double randLonD = (1-(double)(randLon/scale_value)) + meridian -1;
        Log.v(TAG, "Random int Latitude: " + String.valueOf(randLat) + " Longitude: " + String.valueOf(randLon));
        Log.v(TAG, "Random dob Latitude: " + String.valueOf(randLatD) + " Longitude: " + String.valueOf(randLonD));

        return new LatLng(randLatD, randLonD);
    }

    private void addRandomMarkers(GoogleMap googleMap, int num_markers)
    {
        mMap = googleMap;
        for (int i = 0; i < num_markers; i++ )
        {

            mMap.addMarker(new MarkerOptions()
                    .position(randomLongBeachLocation())
                    .title("Parking Instance: " + String.valueOf(i) )
                    .snippet("Hours: 9:00am - 6:00pm\nPrice: $3/hr")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.openparkinglogo_small))
                    .flat(true)
            );

        }
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

        if(mMap != null)
        {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View row = getLayoutInflater().inflate(R.layout.custom_window, null);

                    return null;
                }
            });
        }
        // Replacing this section with users location
        // Add a marker in Long Beach and move the camera
        LatLng longbeach = new LatLng(33.782896, -118.110230);

        mMap.addMarker(new MarkerOptions().position(longbeach).title("Marker in Long Beach"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(longbeach));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));


        // Permission Check
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

        // Create Test Markers for Open Parking Locations
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.782000, -118.110000))
                .title("Hello Marker"));

        // Randomized Markers
        addRandomMarkers(mMap, NUM_MARKERS);



        // Move Camera to LongBeach
        mMap.moveCamera(CameraUpdateFactory.newLatLng(longbeach));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);
    }

    public boolean checkUserLocationPermission()
    {
        // Check if we have permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            // Check if permission has been requested before
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                //Request permission
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;

        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if(currentLocationMarker!= null)
        {
            currentLocationMarker.remove();
        }

        //Location
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        //Marker Settings
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        //Add marker to map
        currentLocationMarker = mMap.addMarker(markerOptions);

        // Move Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        //if
        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    // Update Location
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this );
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Toast.makeText(this, "Info window clicked", Toast.LENGTH_SHORT).show();

    }


}
