package com.example.openparking;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.openparking.Config.RecyclerTouchListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParkingListActivity extends AppCompatActivity{
    private static final String TAG = "ParkingListActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mImagePrice = new ArrayList<>();

    List<ParkingSpace> parkingSpaceList;                    // List of parking spaces that are available for reservation.
    HashMap<String, ParkingSpace> parkingSpaceHashMap;      // For quick look up from marker ID to parkingSpace.
                                                            // Are we still using this HashMap? can I re-purpose it?

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerv_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        parkingSpaceList = new ArrayList<>();
        parkingSpaceHashMap = new HashMap<>();

        // [START initialize_database_ref]
        Log.v(TAG, "getting database reference");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, parkingSpaceList, mImageUrls);
        recyclerView.setAdapter(mAdapter);

        myDialog = new Dialog(this);

        //FireBase
        //loadImagesFromFireBase();
        loadParkingSpacesFromDataBase("90815");

        //Test Info
        setTestImages();
        //setTestData();

        //initRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                showPopup(view);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setTestData()
    {
        mNames.add("132 Fake St.");
        mImagePrice.add("1 Dollars");

        mNames.add("1530 Hackett Ave");
        mImagePrice.add("2 Dollars");

        mNames.add("1541 Hackett Ave");
        mImagePrice.add("3 Dollars");

        mNames.add("Rocky Mountain");
        mImagePrice.add("4 Dollars");

        mNames.add("Mahahual");
        mImagePrice.add("5 Dollars");

        mNames.add("Frozen Lake");
        mImagePrice.add("6 Dollars");

        mNames.add("White Sands Desert");
        mImagePrice.add("7 Dollars");

        mNames.add("Austrailia");
        mImagePrice.add("8 Dollars");

        mNames.add("Washington");
        mImagePrice.add("9 Dollars");
    }

    private void setTestImages()
    {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        //MyAdapter adapter = new MyAdapter(this, mNames, mImageUrls);//TO BE REPLACED
        MyAdapter adapter = new MyAdapter(this, parkingSpaceList, mImageUrls);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadParkingSpacesFromDataBase(String zipCode)
    {
        Log.v(TAG, "loadParkingSpacesFromDataBase");

        mDatabase.child("ParkingSpaces").child(zipCode).addChildEventListener( //OLD TABLE NAME WAS "ZipCodes"
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.v(TAG, "onChildAdded:" + dataSnapshot.getKey());

                        // A new parking space has been added, add it to the displayed list
                        //ParkingSpace ps = new ParkingSpace();
                        ParkingSpace ps = dataSnapshot.getValue(ParkingSpace.class);

                        if(!ps.equals(null))
                        {
                            Log.d(TAG, "onChildAdded: " +  "ps is good");

                            parkingSpaceList.add(ps);
                            Log.v(TAG, "onChildAdded:" + ps.getAddress());
                            mAdapter.notifyDataSetChanged();

                            //String address = ps.getAddress();
                            //String hours = "Hours: " + ps.getOpentime() + " to " + ps.getClosetime();

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

    private void loadImagesFromFireBase()
    {

    }


    public void showPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_window);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
