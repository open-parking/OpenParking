package com.example.openparking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private Marker currentLocationMarker;//To be removed later,
    private static final int Request_User_Location_Code = 99;
    private static final String TAG = "Maps Activity";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    List <ParkingInstance> parkingInstanceList; //List of parking spaces with a car in them
    List <ParkingSpace> parkingSpaceList;       //List of parking spaces that are available for reservation
    
    // [START Test Values for Random Markers in Long Beach]

    // Test Area Latitude and Longitude
    // from (Hilltop Park) to (7th and Studebaker)
    double scale_value  = 1000000.0;
    int parallel = 33;
    int meridian = -118;

    // Latitude Range (numbers must be adjusted with scale_value)
    private static final int maxLat =800471;
    private static final int minLat =774571;
    // Longitude Range(numbers must be adjusted with scale_value)
    private static final int maxLon =167585;
    private static final int minLon =103137;
    private static final int NUM_MARKERS = 8;
    // [END Test Values for Random Markers in Long Beach]


    class ParkingInfoWindow implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        ParkingInfoWindow() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            /**
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }
            render(marker, mWindow);
             **/
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            /**
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
             **/
            return mContents;
        }

    }


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


        parkingInstanceList = new ArrayList<>();
        parkingSpaceList = new ArrayList<>();

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // [END initialize_database_ref]

        loadParkingSpacesFromDataBase("90815");
    }

    // Generates random Latitude Longitude coordinates to be placed in the ParkingInstance ArrayList.
    // These Random Coordinates help test the map markers .
    /*
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
    */


    /** Adds map markers on the map at random coordinates withing Long Beach.
    *   This function is no longer used. It was replaced by:
    *      addRandomMarkersToParkingInstanceList() and
    *      displayMarkersOnList().

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
     **/

    /**
     *  This function is for testing purposes only.
     *  Generates Random Coordinates and saved them to ParkingInstanceList.

    private void addRandomMarkersToParkingInstanceList(int num_markers)
    {
        for (int i = 0; i < num_markers; i++ ) {
            LatLng random_coords = randomLongBeachLocation();
            ParkingInstance testInstance = new ParkingInstance(random_coords);

            parkingInstanceList.add(testInstance);
        }
    }
     **/

    /** Work in Progress
     * TODO: TEST ZIPCODE 90815
     *
     * @param zipCode the zipcode area that we want to load from the database
     */

    private void loadParkingSpacesFromDataBase(String zipCode)
    {

        mDatabase.child("ZipCodes").child(zipCode).addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                        // A new parking space has been added, add it to the displayed list
                        //ParkingSpace ps = new ParkingSpace();
                        ParkingSpace ps = dataSnapshot.getValue(ParkingSpace.class);

                        if(!ps.equals(null))
                        {
                            Log.d(TAG, "onChildAdded: " +  "ps is good");

                            String address = ps.getAddress();
                            String hours = "Hours: " + ps.getOpenTime() + " to " + ps.getCloseTime();

                            //Add marker to map
                            mMap.addMarker(new MarkerOptions()
                                    .position(ps.getLatLng())
                                    .title(address)
                                    .snippet(hours)
                                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.openparkinglogo_small))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .flat(true)
                            );
                        }
                        else
                        {
                            Log.d(TAG, "onChildAdded: " +  "ps is null");
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        //Comment newComment = dataSnapshot.getValue(Comment.class);
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        // commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        //Comment movedComment = dataSnapshot.getValue(Comment.class);
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                        //Toast.makeText(mContext, "Failed to load comments.",
                        //Toast.LENGTH_SHORT).show();
                    }

                });
    }


    /**
     * Displays on the map the markers stored in ParkingInstanceList.
     *
     * @param googleMap

    private void displayMarkersOnList(GoogleMap googleMap)
    {
        mMap = googleMap;
        for (int i = 0; i < parkingInstanceList.size(); i++ )
        {

            mMap.addMarker(new MarkerOptions()
                    .position(parkingInstanceList.get(i).getLatlng())
                    .title("List: " + String.valueOf(i) )
                    .snippet("Hours: 9:00am - 6:00pm\nPrice: $3/hr")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.openparkinglogo_small))
                    .flat(true)
            );
        }
    }
     */


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

        // Replacing this section with users location
        // Add a marker in Long Beach and move the camera
        LatLng longbeach = new LatLng(33.782896, -118.110230);

        //mMap.addMarker(new MarkerOptions().position(longbeach).title("Marker in Long Beach"));// Test Marker
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

        // Test Marker
        //mMap.addMarker(new MarkerOptions()
                //.position(new LatLng(33.782000, -118.110000))
                //.title("Hello Marker"));

        // Randomized Markers
        //addRandomMarkers(mMap, NUM_MARKERS);

        // Load Markers From ParkingInstanceList
        //addRandomMarkersToParkingInstanceList(NUM_MARKERS);
        //displayMarkersOnList(mMap);

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

    public void onMapSearch(View view) {
        
        EditText addressSearchText = findViewById(R.id.address_search_text);
        String location = addressSearchText.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!addressList.isEmpty())
            {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                //Marker Options
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Search Result");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                mMap.addMarker(markerOptions);

                //mMap.addMarker(new MarkerOptions().position(latLng).title("Search Result"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }else
            {
                Log.v(TAG, "onMapSearch: addressList isEmpty()");
            }

        }
    }

    public String getUid() {
        return "42";
    }

    public void basicQueryValueListener() {
        String myUserId = getUid();
        String myZipcode = "90840";

        Query myParkingInstanceQuery = mDatabase.child("parking-instances").child(myZipcode);

        /**Query myParkingInstanceQuery = mDatabase.child("user-posts").child(myUserId)
                .orderByChild("starCount");**/

        // [START basic_query_value_listener]
        // My top posts by number of stars
        myParkingInstanceQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    //postSnapshot.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        // [END basic_query_value_listener]
    }

    @Override
    public void onStart() {
        super.onStart();
        basicQueryValueListener();
    }


}
